package com.example.newsapp.root.builder

import com.example.newsapp.root.RootInteractor
import com.example.newsapp.root.RootRouter
import com.example.newsapp.root.RootView
import com.example.newsapp.root.di.DaggerRootComponent
import com.example.newsapp.root.di.RootComponent
import com.uber.rib.core.Builder

class RootBuilder(dependencies: RootDependencies) :
    Builder<RootRouter, RootDependencies>(dependencies){
    fun build(): RootRouter {
        val presenter = RootView()
        val interactor = RootInteractor()
        val component: RootComponent = DaggerRootComponent.builder()
            .dependencies(dependency)
            .view(presenter)
            .interactor(interactor)
            .build()
        return component.router()
    }
}