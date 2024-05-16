package com.example.newsapp.root.units.newslist.builder

import com.example.newsapp.root.units.newslist.NewsListEvents
import kotlinx.coroutines.flow.FlowCollector

interface NewsListDependencies {
    fun newsListEventsFlowCollector() : FlowCollector<@JvmSuppressWildcards NewsListEvents>
}