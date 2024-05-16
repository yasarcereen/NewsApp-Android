package com.example.newsapp.root.units.newslist.di

import com.example.newsapp.root.units.newslist.NewsListInteractor
import com.example.newsapp.root.units.newslist.NewsListRouter
import com.example.newsapp.root.units.newslist.NewsListView
import com.example.newsapp.root.units.newslist.builder.NewsListDependencies
import com.uber.rib.core.InteractorBaseComponent
import dagger.BindsInstance
import dagger.Component

@NewsListScope
@Component(modules = [NewsListModule::class], dependencies = [NewsListDependencies::class])
interface NewsListComponent: InteractorBaseComponent<NewsListInteractor> {
    fun router(): NewsListRouter

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun interactor(interactor: NewsListInteractor): Builder

        @BindsInstance
        fun view(view: NewsListView): Builder

        fun dependencies(dependencies: NewsListDependencies): Builder

        fun build(): NewsListComponent
    }
}
