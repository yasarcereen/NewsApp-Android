package com.example.newsapp.root.units.newslist.di

import com.example.newsapp.root.units.newslist.NewsListEvents
import com.example.newsapp.root.units.newslist.NewsListInteractor
import com.example.newsapp.root.units.newslist.NewsListRouter
import com.example.newsapp.root.units.newslist.NewsListView
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow

@Module
abstract class NewsListModule {
    @NewsListScope
    companion object {
        @Provides
        fun router(
            component: NewsListComponent,
            view: NewsListView,
            interactor: NewsListInteractor
        ) : NewsListRouter {
            return NewsListRouter(view, interactor, component)
        }
    }

    @NewsListScope
    @Binds
    abstract fun presenter(view: NewsListView): NewsListInteractor.Presenter
}