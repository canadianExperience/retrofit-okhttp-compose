package com.me.retrofit_okhttp_compose.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.outlined.CheckBoxOutlineBlank
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.me.retrofit_okhttp_compose.data.apimanager.model.Todo
import com.me.retrofit_okhttp_compose.ui.theme.RetrofitokhttpcomposeTheme
import com.me.retrofit_okhttp_compose.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RetrofitokhttpcomposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel = hiltViewModel<MainViewModel>()
                    val todosState = viewModel.todos.collectAsState(emptyList())

                    AppBody(todos = todosState.value)
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
private fun AppBody(todos: List<Todo>){

    Column(
        modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            fontWeight = FontWeight.Bold,
            text = "Retrofit Call",
            modifier = Modifier.padding(all = 8.dp)
        )
        MyList(todos)
    }
}

@ExperimentalMaterial3Api
@Composable
private fun MyList(todos: List<Todo>){
    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        this.items(items = todos){ todo ->
            TodoItem(
                title = todo.title,
                completed = todo.completed
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
private fun TodoItem(title: String, completed: Boolean){
    Card(modifier = Modifier
        .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
        .clip(RoundedCornerShape(10.dp))
        .clickable(onClick = {})
        .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                maxLines = 1,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f),
            )
            Spacer(modifier = Modifier.width(16.dp))
            IconToggleButton(
                checked = completed,
                onCheckedChange = {}
            ) {
                Icon(
                    imageVector = if(completed) Icons.Outlined.CheckBox else Icons.Outlined.CheckBoxOutlineBlank,
                    contentDescription = "Check box",
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 300,
    heightDp = 500
)
@ExperimentalMaterial3Api
@Composable
fun DefaultPreview() {
    RetrofitokhttpcomposeTheme {
        AppBody(listOf())
    }
}

@ExperimentalMaterial3Api
@Preview(
    showBackground = true,
    widthDp = 300,
    heightDp = 50
)
@Composable
fun TodoItemPreview() {
    RetrofitokhttpcomposeTheme {
        TodoItem(title = "One", completed = true)
    }
}