package com.example.newsapp.data.api

import com.example.newsapp.data.model.Response
import retrofit2.Call
import retrofit2.http.GET

interface NewsApi {
    @GET("https://newsapi.org/v2/everything?q=tesla&from=2024-04-15&sortBy=publishedAt&apiKey=e258338855f64eb6aacd7c0bd290a4b9")
    suspend fun allNews(): Response
}