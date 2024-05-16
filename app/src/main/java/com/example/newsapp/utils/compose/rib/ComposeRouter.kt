package com.example.newsapp.utils.compose.rib

import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.InteractorBaseComponent
import com.uber.rib.core.RibRefWatcher
import com.uber.rib.core.Router

abstract class ComposeRouter<V : Compose, I : Interactor<*, *>> : Router<I> {
    /** @return the router's view. */
    open val view: V

    constructor(view: V, interactor: I, component: InteractorBaseComponent<*>) : super(interactor, component) {
        this.view = view
    }

    protected constructor(view: V, interactor: I) : super(null, interactor,
        RibRefWatcher.getInstance(), getMainThread()
    ) {
        this.view = view
    }

    internal fun saveInstanceStateInternal(outState: Bundle) {
        saveInstanceState(outState)
    }
}
