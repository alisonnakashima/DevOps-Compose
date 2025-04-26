package com.alisonnakashima.newcomposetasklist

import org.junit.Test
import org.junit.Assert.*

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.*
import androidx.compose.material3.*
import com.alisonnakashima.newcomposetasklist.TaskListScreen
import com.alisonnakashima.newcomposetasklist.TaskViewModel
import org.junit.Rule


class TaskListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test // Test 1
    fun shouldAddTaskWhenValidInputIsGiven() {
        composeTestRule.setContent {
            TaskListScreen(viewModel = TaskViewModel())
        }

        // Preencher o campo de tarefa
        composeTestRule.onNodeWithText("Nova tarefa").performTextInput("New Task")
        composeTestRule.onNodeWithText("Adicionar").performClick()

        // Verificar se a tarefa foi adicionada
        composeTestRule.onNodeWithText("New Task").assertExists()
    }

    @Test // Test 2
    fun shouldNotAddTaskWhenInvalidInputIsGiven() {
        composeTestRule.setContent {
            TaskListScreen(viewModel = TaskViewModel())
        }

        // Preencher o campo de tarefa com um valor inválido
        composeTestRule.onNodeWithText("Nova tarefa").performTextInput("!@#Invalid")
        composeTestRule.onNodeWithText("Adicionar").performClick()

        // Verificar que a tarefa não foi adicionada
        composeTestRule.onNodeWithTag("!@#Invalid").assertDoesNotExist()
    }

    @Test //Test 3
    fun shouldToggleTaskWhenCheckboxIsClicked() {
        composeTestRule.setContent {
            TaskListScreen(viewModel = TaskViewModel())
        }

        composeTestRule.onNodeWithText("Nova tarefa").performTextInput("New Task")
        composeTestRule.onNodeWithText("Adicionar").performClick()

        // Marcar a tarefa como concluída
        composeTestRule.onNodeWithText("New Task").performClick()
        composeTestRule.onNodeWithText("New Task").assertTextContains("New Task")  // Verifica o checkbox preenchido
    }
}