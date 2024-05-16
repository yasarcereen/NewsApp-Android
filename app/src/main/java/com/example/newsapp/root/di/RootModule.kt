package com.example.newsapp.root.di

import com.example.newsapp.root.RootInteractor
import com.example.newsapp.root.RootRouter
import com.example.newsapp.root.RootView
import com.example.newsapp.root.units.article.ArticleEvents
import com.example.newsapp.root.units.article.builder.ArticleBuilder
import com.example.newsapp.root.units.newslist.NewsListEvents
import com.example.newsapp.root.units.newslist.builder.NewsListBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow

@Module
abstract class RootModule {
    companion object {
        @RootScope
        @Provides
        fun router(
            component: RootComponent,
            view: RootView,
            interactor: RootInteractor
        ): RootRouter {
            return RootRouter(view,
                interactor,
                component,
                NewsListBuilder(component),
                ArticleBuilder(component)
            )
        }

        @RootScope
        @Provides
        fun newsListEventsMutableFlow(): MutableSharedFlow<@JvmSuppressWildcards NewsListEvents> {
            return MutableSharedFlow()
        }

        @RootScope
        @Provides
        fun articleEventsMutableFlow(): MutableSharedFlow<@JvmSuppressWildcards ArticleEvents> {
            return MutableSharedFlow()
        }
    }

    @RootScope
    @Binds
    abstract fun presenter(view: RootView): RootInteractor.Presenter

    @RootScope
    @Binds
    abstract fun newsListEventsAsFlow(
        mutableFlow: MutableSharedFlow<@JvmSuppressWildcards NewsListEvents>
    ): Flow<@JvmSuppressWildcards NewsListEvents>

    @RootScope
    @Binds
    abstract fun newsListEventsAsFlowCollector(
        mutableFlow: MutableSharedFlow<@JvmSuppressWildcards NewsListEvents>
    ): FlowCollector<@JvmSuppressWildcards NewsListEvents>

    @RootScope
    @Binds
    abstract fun articleEventsAsFlow(
        mutableFlow: MutableSharedFlow<@JvmSuppressWildcards ArticleEvents>
    ): Flow<@JvmSuppressWildcards ArticleEvents>

    @RootScope
    @Binds
    abstract fun articleEventsAsFlowCollector(
        mutableFlow: MutableSharedFlow<@JvmSuppressWildcards ArticleEvents>
    ): FlowCollector<@JvmSuppressWildcards ArticleEvents>
}
