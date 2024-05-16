package com.example.newsapp.root

import com.example.newsapp.root.units.article.ArticleEvents
import com.example.newsapp.root.units.newslist.NewsListEvents
import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class RootInteractor : Interactor<RootInteractor.Presenter, RootRouter>() {

    @Inject
    lateinit var newsListEventsFlow: Flow<@JvmSuppressWildcards NewsListEvents>

    @Inject
    lateinit var articleEventsFlow: Flow<@JvmSuppressWildcards ArticleEvents>

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        router.attachNewsList()

        coroutineScope.launch {
            newsListEventsFlow.collect{
                when (it) {
                    is NewsListEvents.ArticleSelected -> {
                        router.detachNewsList()
                        router.attachArticle(it.article)
                    }
                }
            }
        }

        coroutineScope.launch {
            articleEventsFlow.collect {
                when (it) {
                    is ArticleEvents.BackButtonClicked -> {
                        router.detachArticle()
                        router.attachNewsList()
                    }
                }
            }
        }
    }

    interface Presenter
}