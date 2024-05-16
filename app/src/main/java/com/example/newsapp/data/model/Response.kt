package com.example.newsapp.data.model

data class Response (
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)