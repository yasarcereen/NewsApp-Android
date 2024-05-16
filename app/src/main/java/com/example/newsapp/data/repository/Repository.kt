package com.example.newsapp.data.repository

import com.example.newsapp.data.api.RetrofitInstance
import com.example.newsapp.data.model.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class Repository {
    suspend fun getAllNews(): Flow<List<Article>> {
        return flow {
            emit(RetrofitInstance.api.allNews().articles)
        }
    }

    suspend fun getNumberOfNews(): Flow<Int> {
        return flow {
            emit(RetrofitInstance.api.allNews().totalResults)
        }
    }

    suspend fun getStatus(): Flow<String> {
        return flow {
            emit(RetrofitInstance.api.allNews().status)
        }
    }
}