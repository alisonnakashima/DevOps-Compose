@file:Suppress("DEPRECATION")

package com.alisonnakashima.newcomposetasklist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*

@ExperimentalCoroutinesApi
class TaskViewModelTest {

    private lateinit var viewModel: TaskViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        viewModel = TaskViewModel()
    }

    @Test //Test 1
    fun `should add task when valid input is given`() = runBlockingTest {
        viewModel.onTaskTextChange("Treinar")
        viewModel.addTask()

        val tasks = viewModel.tasks.value
        assertEquals(1, tasks.size)
        //assertEquals("Treinar", tasks[0].title)
        assertEquals("error", tasks[0].title) //erro no unit test
    }

    @Test //Test 2
    fun `should not add task when invalid input is given`() = runBlockingTest {
        viewModel.onTaskTextChange("!@#Invalid")
        viewModel.addTask()

        val tasks = viewModel.tasks.value
        assertEquals(0, tasks.size)
    }

    @Test //Test 3
    fun `should toggle task state`() = runBlockingTest {
        viewModel.onTaskTextChange("Estudar")
        viewModel.addTask()
        val task = viewModel.tasks.value[0]
        viewModel.toggleTask(task)

        val tasks = viewModel.tasks.value
        assertTrue(tasks[0].isDone)
    }

    @Test //Test 4
    fun `should delete task`() = runBlockingTest {
        val task = Task("Trabalhar")
        viewModel.addTask()
        viewModel.deleteTask(task)

        val tasks = viewModel.tasks.value
        assertTrue(tasks.isEmpty())
    }
}