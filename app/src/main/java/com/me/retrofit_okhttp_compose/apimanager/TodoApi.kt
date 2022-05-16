package com.me.retrofit_okhttp_compose.apimanager

import com.me.retrofit_okhttp_compose.apimanager.model.Todo
import retrofit2.Response
import retrofit2.http.GET

interface TodoApi {
//    https://jsonplaceholder.typicode.com/todos

    @GET("/todos")
    suspend fun getTodos(): Response<List<Todo>>
}