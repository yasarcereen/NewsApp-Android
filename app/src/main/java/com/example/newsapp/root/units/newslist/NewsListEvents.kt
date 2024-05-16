package com.example.newsapp.root.units.newslist

import com.example.newsapp.data.model.Article

sealed interface NewsListEvents {
    data class ArticleSelected(val article: Article) : NewsListEvents
}