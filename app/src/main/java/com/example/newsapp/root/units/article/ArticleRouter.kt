package com.example.newsapp.root.units.article

import com.example.newsapp.utils.compose.rib.ComposeRouter
import com.uber.rib.core.InteractorBaseComponent

class ArticleRouter(
    view: ArticleView,
    interactor: ArticleInteractor,
    component: InteractorBaseComponent<*>
) : ComposeRouter<ArticleView, ArticleInteractor>(view, interactor, component) {
}