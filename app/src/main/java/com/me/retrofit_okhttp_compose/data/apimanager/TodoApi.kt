package com.me.retrofit_okhttp_compose.data.apimanager

import com.me.retrofit_okhttp_compose.data.apimanager.model.Todo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface TodoApi {
//    https://jsonplaceholder.typicode.com/todos

    @GET("/todos")
    suspend fun getTodos(): Response<List<Todo>>
}