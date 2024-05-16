package com.example.newsapp.root.units.article.di

import com.example.newsapp.data.model.Article
import com.example.newsapp.root.units.article.ArticleInteractor
import com.example.newsapp.root.units.article.ArticleRouter
import com.example.newsapp.root.units.article.ArticleView
import com.example.newsapp.root.units.article.di.ArticleComponent
import com.example.newsapp.root.units.article.di.ArticleScope
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class ArticleModule {
    @ArticleScope
    companion object {
        @Provides
        fun router(
            component: ArticleComponent,
            view: ArticleView,
            interactor: ArticleInteractor
        ): ArticleRouter {
            return ArticleRouter(view, interactor, component)
        }
    }

    @ArticleScope
    @Binds
    abstract fun presenter(view: ArticleView): ArticleInteractor.Presenter
}