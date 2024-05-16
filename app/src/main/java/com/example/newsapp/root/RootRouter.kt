package com.example.newsapp.root

import com.example.newsapp.data.model.Article
import com.example.newsapp.root.units.article.ArticleRouter
import com.example.newsapp.root.units.article.builder.ArticleBuilder
import com.example.newsapp.root.units.newslist.NewsListRouter
import com.example.newsapp.root.units.newslist.builder.NewsListBuilder
import com.example.newsapp.utils.compose.rib.ComposeRouter
import com.uber.rib.core.InteractorBaseComponent

class RootRouter(
    view: RootView,
    interactor: RootInteractor,
    component: InteractorBaseComponent<*>,
    private val newsListBuilder: NewsListBuilder,
    private val articleBuilder: ArticleBuilder
) : ComposeRouter<RootView, RootInteractor>(view, interactor, component) {
    private var newsListRouter : NewsListRouter ?= null

    private var articleRouter: ArticleRouter ?= null

    fun attachNewsList() {
        if (newsListRouter != null) {
            throw IllegalStateException("News list is already attached")
        }

        newsListRouter = newsListBuilder.build().also {
            attachChild(it)
            view.setUpdateView(it.view)
        }
    }

    fun detachNewsList() {
        if (newsListRouter == null) {
            throw  IllegalStateException("News list is not attached yet")
        }

        newsListRouter?.let {
            detachChild(it)
            view.removeContainerView()
        }
        newsListRouter = null
    }

    fun attachArticle(article: Article) {
        if (articleRouter != null) {
            throw IllegalStateException("Article is already attached")
        }

        articleRouter = articleBuilder.build(article).also {
            attachChild(it)
            view.setUpdateView(it.view)
        }
    }

    fun detachArticle() {
        if (articleRouter == null) {
            throw IllegalStateException("Article is not attached yet")
        }

        articleRouter?.let {
            detachChild(it)
            view.removeContainerView()
        }
        articleRouter = null
    }
}