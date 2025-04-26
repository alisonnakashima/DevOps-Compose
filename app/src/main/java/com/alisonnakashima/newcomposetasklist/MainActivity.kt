package com.alisonnakashima.newcomposetasklist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.activity.viewModels
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import com.alisonnakashima.newcomposetasklist.TaskListScreen as TaskListScreen

data class Task(val title: String, val isDone: Boolean = false)

class TaskViewModel : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    private val _newTask = MutableStateFlow("")
    val newTask: StateFlow<String> = _newTask

    private val _hasInputError = MutableStateFlow(false)
    val hasInputError: StateFlow<Boolean> = _hasInputError

    fun onTaskTextChange(newValue: String) {
        _newTask.value = newValue
    }

    fun addTask() {
        val task = _newTask.value.trim()
        if (task.isEmpty() || task.contains(Regex("[@#!\$%¨&*]"))) {
            _hasInputError.value = true
        } else {
            _tasks.update { it + Task(task) }
            _newTask.value = ""
            _hasInputError.value = false
        }
    }

    fun toggleTask(task: Task) {
        _tasks.update { list ->
            list.map {
                if (it == task) it.copy(isDone = !it.isDone) else it
            }
        }
    }

    fun deleteTask(task: Task) {
        _tasks.update { it - task }
    }
}

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<TaskViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskListScreen(viewModel = viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(viewModel: TaskViewModel) {
    val tasks by viewModel.tasks.collectAsState()
    val newTask by viewModel.newTask.collectAsState()
    val hasError by viewModel.hasInputError.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Tarefas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = newTask,
                    onValueChange = {
                        viewModel.onTaskTextChange(it)
                    },
                    label = { Text("Nova tarefa") },
                    isError = hasError,
                    supportingText = {
                        if (hasError) {
                            Text("Campo vazio ou com símbolos [@#!\$%¨&*]")
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { viewModel.addTask() }) {
                    Text("Adicionar")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(tasks) { task ->
                    TaskItem(
                        task = task,
                        onDelete = { viewModel.deleteTask(task) },
                        onToggle = { viewModel.toggleTask(task) }
                    )
                }
            }
        }
    }
}

@Composable
fun TaskItem(task: Task, onDelete: (Task) -> Unit, onToggle: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .testTag("taskItem_${task.title}")
    ) {
        Checkbox(
            checked = task.isDone,
            onCheckedChange = { onToggle() }
        )
        Text(
            text = task.title,
            modifier = Modifier.weight(1f),
            textDecoration = if (task.isDone) TextDecoration.LineThrough else TextDecoration.None
        )
        IconButton(onClick = { onDelete(task) }) {
            Icon(Icons.Default.Delete, contentDescription = "Deletar")
        }
    }
}