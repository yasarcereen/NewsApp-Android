package com.example.newsapp.root.units.article.builder

import com.example.newsapp.root.units.article.ArticleEvents
import kotlinx.coroutines.flow.FlowCollector

interface ArticleDependencies {
    fun articleEvents(): FlowCollector<@JvmSuppressWildcards ArticleEvents>
}