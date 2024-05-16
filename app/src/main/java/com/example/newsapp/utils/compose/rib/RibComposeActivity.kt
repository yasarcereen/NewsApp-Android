package com.example.newsapp.utils.compose.rib

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import androidx.activity.compose.setContent
import androidx.annotation.CallSuper
import androidx.annotation.RequiresApi
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.ui.Modifier
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.uber.autodispose.lifecycle.CorrespondingEventsFunction
import com.uber.autodispose.lifecycle.LifecycleEndedException
import com.uber.autodispose.lifecycle.LifecycleScopeProvider
import com.uber.autodispose.lifecycle.LifecycleScopes
import com.uber.rib.core.ActivityStarter
import com.uber.rib.core.Bundle
import com.uber.rib.core.CoreAppCompatActivity
import com.uber.rib.core.Initializer
import com.uber.rib.core.Interactor
import com.uber.rib.core.RibEventType
import com.uber.rib.core.RibEvents
import com.uber.rib.core.RxActivityEvents
import com.uber.rib.core.lifecycle.ActivityCallbackEvent
import com.uber.rib.core.lifecycle.ActivityLifecycleEvent
import io.reactivex.CompletableSource
import io.reactivex.Observable
import java.lang.ref.WeakReference

abstract class RibComposeActivity : CoreAppCompatActivity(), ActivityStarter,
    LifecycleScopeProvider<ActivityLifecycleEvent>,
    RxActivityEvents {
    private var router: ComposeRouter<*, *>? = null
    private val lifecycleBehaviorRelay = BehaviorRelay.create<ActivityLifecycleEvent>()
    private val lifecycleRelay: Relay<ActivityLifecycleEvent> = lifecycleBehaviorRelay.toSerialized()
    private val callbacksRelay = PublishRelay.create<ActivityCallbackEvent>().toSerialized()

    /** @return an observable of this activity's lifecycle events. */
    override fun lifecycle(): Observable<ActivityLifecycleEvent> {
        return lifecycleRelay.hide()
    }

    /** @return an observable of this activity's lifecycle events. */
    override fun callbacks(): Observable<ActivityCallbackEvent> {
        return callbacksRelay.hide()
    }

    override fun correspondingEvents(): CorrespondingEventsFunction<ActivityLifecycleEvent> {
        return ACTIVITY_LIFECYCLE
    }

    override fun peekLifecycle(): ActivityLifecycleEvent {
        return lifecycleBehaviorRelay.value!!
    }

    override fun requestScope(): CompletableSource {
        return LifecycleScopes.resolveScopeFromLifecycle(this)
    }

    var compositionContext: WeakReference<CompositionContext>? = null

    @Initializer
    @CallSuper
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleRelay.accept(ActivityLifecycleEvent.createOnCreateEvent(savedInstanceState))
        val wrappedBundle: Bundle? = if (savedInstanceState != null) Bundle(savedInstanceState) else null
        router = createRouter()
        router?.let {
            it.dispatchAttach(wrappedBundle)
            setContent {
                compositionContext = WeakReference(rememberCompositionContext())
                it.view.Content(Modifier)
            }
            RibEvents.getInstance().emitEvent(RibEventType.ATTACHED, it, null)
        }
    }

    @CallSuper
    override fun onSaveInstanceState(outState: android.os.Bundle) {
        super.onSaveInstanceState(outState)
        callbacksRelay.accept(ActivityCallbackEvent.createOnSaveInstanceStateEvent(outState))
        router?.saveInstanceStateInternal(Bundle(outState)) ?: throw NullPointerException("Router should not be null")
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        lifecycleRelay.accept(ActivityLifecycleEvent.create(ActivityLifecycleEvent.Type.START))
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        lifecycleRelay.accept(ActivityLifecycleEvent.create(ActivityLifecycleEvent.Type.RESUME))
    }

    @CallSuper
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        callbacksRelay.accept(ActivityCallbackEvent.createNewIntent(intent))
    }

    @CallSuper
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbacksRelay.accept(
            ActivityCallbackEvent.createOnActivityResultEvent(
                requestCode,
                resultCode,
                data
            )
        )
    }

    @CallSuper
    override fun onPause() {
        lifecycleRelay.accept(ActivityLifecycleEvent.create(ActivityLifecycleEvent.Type.PAUSE))
        super.onPause()
    }

    @CallSuper
    override fun onStop() {
        lifecycleRelay.accept(ActivityLifecycleEvent.create(ActivityLifecycleEvent.Type.STOP))
        super.onStop()
    }

    @CallSuper
    override fun onDestroy() {
        lifecycleRelay.accept(ActivityLifecycleEvent.create(ActivityLifecycleEvent.Type.DESTROY))
        router?.let {
            it.dispatchDetach()
            RibEvents.getInstance().emitEvent(RibEventType.DETACHED, it, null)
        }
        router = null
        super.onDestroy()
    }

    @CallSuper
    override fun onLowMemory() {
        super.onLowMemory()
        callbacksRelay.accept(ActivityCallbackEvent.create(ActivityCallbackEvent.Type.LOW_MEMORY))
    }

    @CallSuper
    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        callbacksRelay.accept(ActivityCallbackEvent.createTrimMemoryEvent(level))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        callbacksRelay.accept(
            ActivityCallbackEvent.createPictureInPictureMode(isInPictureInPictureMode)
        )
    }

    override fun onBackPressed() {
        if (router?.handleBackPress() != true) {
            onUnhandledBackPressed()

            // https://issuetracker.google.com/issues/139738913
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && isTaskRoot &&
                supportFragmentManager.backStackEntryCount == 0
            ) {
                super.finishAfterTransition()
            } else {
                super.onBackPressed()
            }
        }
    }

    override fun onUserLeaveHint() {
        lifecycleRelay.accept(ActivityLifecycleEvent.create(ActivityLifecycleEvent.Type.USER_LEAVING))
        super.onUserLeaveHint()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        callbacksRelay.accept(ActivityCallbackEvent.createWindowFocusEvent(hasFocus))
    }

    /**
     * Invoked when none of the ribs handle back press. In this case, default activity back press
     * behavior occurs.
     */
    protected open fun onUnhandledBackPressed() {}

    /**
     * @return the [Interactor] when the activity has alive.
     * @throws IllegalStateException if the activity has not been created or has been destroyed.
     */
    open val interactor: Interactor<*, *>
        get() = if (router != null) {
            router?.interactor as Interactor<*, *>
        } else {
            throw IllegalStateException(
                "Attempting to get a router when activity is not created or has been destroyed."
            )
        }

    /**
     * Creates the [Interactor].
     *
     * @return the [Interactor].
     */
    protected abstract fun createRouter(): ComposeRouter<*, *>

    companion object {
        /**
         * Figures out which corresponding next lifecycle event in which to unsubscribe, for Activities.
         */
        private val ACTIVITY_LIFECYCLE = CorrespondingEventsFunction { lastEvent: ActivityLifecycleEvent ->
            return@CorrespondingEventsFunction when (lastEvent.type) {
                ActivityLifecycleEvent.Type.CREATE -> ActivityLifecycleEvent.create(
                    ActivityLifecycleEvent.Type.DESTROY
                )
                ActivityLifecycleEvent.Type.START -> ActivityLifecycleEvent.create(
                    ActivityLifecycleEvent.Type.STOP
                )
                ActivityLifecycleEvent.Type.RESUME -> ActivityLifecycleEvent.create(
                    ActivityLifecycleEvent.Type.PAUSE
                )
                ActivityLifecycleEvent.Type.USER_LEAVING -> ActivityLifecycleEvent.create(
                    ActivityLifecycleEvent.Type.DESTROY
                )
                ActivityLifecycleEvent.Type.PAUSE -> ActivityLifecycleEvent.create(
                    ActivityLifecycleEvent.Type.STOP
                )
                ActivityLifecycleEvent.Type.STOP -> ActivityLifecycleEvent.create(
                    ActivityLifecycleEvent.Type.DESTROY
                )
                ActivityLifecycleEvent.Type.DESTROY -> throw LifecycleEndedException(
                    "Cannot bind to Activity lifecycle when outside of it."
                )
            }
        }
    }
}
