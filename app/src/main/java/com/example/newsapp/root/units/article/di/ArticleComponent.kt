package com.example.newsapp.root.units.article.di

import com.example.newsapp.data.model.Article
import com.example.newsapp.root.units.article.ArticleInteractor
import com.example.newsapp.root.units.article.ArticleRouter
import com.example.newsapp.root.units.article.ArticleView
import com.example.newsapp.root.units.article.builder.ArticleDependencies
import com.example.newsapp.root.units.article.di.ArticleModule
import com.example.newsapp.root.units.article.di.ArticleScope
import com.uber.rib.core.InteractorBaseComponent
import dagger.BindsInstance
import dagger.Component

@ArticleScope
@Component(modules = [ArticleModule::class], dependencies = [ArticleDependencies::class])
interface ArticleComponent: InteractorBaseComponent<ArticleInteractor> {
    fun router(): ArticleRouter

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun interactor(interactor: ArticleInteractor): Builder

        @BindsInstance
        fun view(view: ArticleView): Builder

        fun dependencies(dependencies: ArticleDependencies): Builder

        fun build(): ArticleComponent
    }
}