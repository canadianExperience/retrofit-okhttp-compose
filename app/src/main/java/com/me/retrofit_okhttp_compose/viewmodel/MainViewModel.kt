package com.me.retrofit_okhttp_compose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.me.retrofit_okhttp_compose.data.apimanager.NetworkResult
import com.me.retrofit_okhttp_compose.data.apimanager.model.Todo
import com.me.retrofit_okhttp_compose.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
)  : ViewModel() {

    init {
        getTodos()
    }

    private val todosFlow = MutableStateFlow(listOf<Todo>())
    val todos get() = todosFlow.asStateFlow()

    private fun getTodos() = viewModelScope.launch {
       val response = repository.remote.getTodos()
        handleNetworkResponse(response)?.let { result ->
            val todos = result.data
            todos?.let {
                val limitList = it.subList(0,10)
                todosFlow.emit(limitList)
            }
        }
    }

    private fun handleNetworkResponse(response: Response<List<Todo>>): NetworkResult<List<Todo>>?{
        when{
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited")
            }
            response.body().isNullOrEmpty() -> {
                return NetworkResult.Error("Todos Not Found")
            }
            response.isSuccessful -> {
                val todos = response.body()
                return NetworkResult.Success(todos!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }
}