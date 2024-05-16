package com.example.newsapp.root.units.article

import com.example.newsapp.data.model.Article
import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.coroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ArticleInteractor(private val selectedArticle: Article): Interactor<ArticleInteractor.Presenter, ArticleRouter>() {
    @Inject
    lateinit var articleEventsFlowCollector: FlowCollector<@JvmSuppressWildcards ArticleEvents>

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)

        coroutineScope.launch {
            injectedPresenter.article.emit(selectedArticle)
        }

        coroutineScope.launch {
            injectedPresenter.backButtonClicks.collect {
                articleEventsFlowCollector.emit(ArticleEvents.BackButtonClicked)
            }
        }
    }

    interface Presenter {
        val article: MutableStateFlow<Article?>
        val backButtonClicks: MutableSharedFlow<Unit>
    }
}