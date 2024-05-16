package com.example.newsapp.root.units.newslist

import com.example.newsapp.utils.compose.rib.ComposeRouter
import com.uber.rib.core.InteractorBaseComponent
import com.uber.rib.core.Router

class NewsListRouter(
    view: NewsListView,
    interactor: NewsListInteractor,
    component: InteractorBaseComponent<*>
) : ComposeRouter<NewsListView, NewsListInteractor>(view, interactor, component) {
}