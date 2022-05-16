package com.me.retrofit_okhttp_compose.data.repository

import com.me.retrofit_okhttp_compose.data.apimanager.TodoApi
import com.me.retrofit_okhttp_compose.data.apimanager.model.Todo
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val todoApi: TodoApi
) {
    suspend fun getTodos(): Response<List<Todo>> = todoApi.getTodos()
}