package com.example.newsapp.root.di

import com.example.newsapp.root.RootInteractor
import com.example.newsapp.root.RootRouter
import com.example.newsapp.root.RootView
import com.example.newsapp.root.builder.RootDependencies
import com.example.newsapp.root.units.article.builder.ArticleDependencies
import com.example.newsapp.root.units.newslist.builder.NewsListDependencies
import com.uber.rib.core.InteractorBaseComponent
import dagger.BindsInstance
import dagger.Component

@RootScope
@Component(modules = [RootModule::class], dependencies = [RootDependencies::class])
interface RootComponent: InteractorBaseComponent<RootInteractor>, NewsListDependencies, ArticleDependencies {
    fun router() : RootRouter

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun interactor(interactor: RootInteractor): Builder

        @BindsInstance
        fun view(view: RootView): Builder

        fun dependencies(dependencies: RootDependencies): Builder

        fun build(): RootComponent
    }
}
