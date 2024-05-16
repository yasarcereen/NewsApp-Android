package com.example.newsapp.root.units.newslist.builder

import com.example.newsapp.root.units.newslist.NewsListInteractor
import com.uber.rib.core.Builder
import com.example.newsapp.root.units.newslist.NewsListRouter
import com.example.newsapp.root.units.newslist.NewsListView
import com.example.newsapp.root.units.newslist.di.NewsListComponent
import com.example.newsapp.root.units.newslist.di.DaggerNewsListComponent

class NewsListBuilder(dependencies: NewsListDependencies) :
    Builder<NewsListRouter, NewsListDependencies>(dependencies){
    fun build(): NewsListRouter {
        val presenter = NewsListView()
        val interactor = NewsListInteractor()
        val component: NewsListComponent = DaggerNewsListComponent.builder()
            .dependencies(dependency)
            .view(presenter)
            .interactor(interactor)
            .build()
        return component.router()
    }
}