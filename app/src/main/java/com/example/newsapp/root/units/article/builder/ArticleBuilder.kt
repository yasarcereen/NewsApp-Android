package com.example.newsapp.root.units.article.builder

import com.example.newsapp.data.model.Article
import com.example.newsapp.root.units.article.ArticleInteractor
import com.example.newsapp.root.units.article.ArticleRouter
import com.example.newsapp.root.units.article.ArticleView
import com.example.newsapp.root.units.article.builder.ArticleDependencies
import com.example.newsapp.root.units.article.di.DaggerArticleComponent
import com.example.newsapp.root.units.article.di.ArticleComponent
import com.uber.rib.core.Builder

class ArticleBuilder(dependencies: ArticleDependencies) :
    Builder<ArticleRouter, ArticleDependencies>(dependencies){
    fun build(article: Article): ArticleRouter {
        val presenter = ArticleView()
        val interactor = ArticleInteractor(article)
        val component: ArticleComponent = DaggerArticleComponent.builder()
            .dependencies(dependency)
            .view(presenter)
            .interactor(interactor)
            .build()
        return component.router()
    }
}