package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AiAssistantDialog
import com.example.ui.components.SchoolHeader
import com.example.ui.screens.ExamPreviewScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MatrixSpecScreen
import com.example.ui.screens.QuestionBankScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.WizardScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.ExamViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: ExamViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: ExamViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val notification by viewModel.notification.collectAsStateWithLifecycle()
    val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()
    val isAiThinking by viewModel.isAiThinking.collectAsStateWithLifecycle()

    var showAiAssistantDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(notification) {
        notification?.let {
            snackbarHostState.showSnackbar(it.message)
            viewModel.clearNotification()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            SchoolHeader(
                title = "THCS LONG XUYÊN",
                subtitle = "ỨNG DỤNG XÂY DỰNG ĐỀ KIỂM TRA",
                showHomeButton = currentScreen != AppScreen.HOME,
                onHomeClick = { viewModel.goToHome() },
                onAssistantClick = { showAiAssistantDialog = true },
                onSettingsClick = { viewModel.navigateTo(AppScreen.SETTINGS) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                AppScreen.HOME -> HomeScreen(viewModel = viewModel)
                AppScreen.WIZARD -> WizardScreen(viewModel = viewModel)
                AppScreen.QUESTION_BANK -> QuestionBankScreen(viewModel = viewModel)
                AppScreen.MATRIX_SPEC -> MatrixSpecScreen(viewModel = viewModel)
                AppScreen.HISTORY -> HistoryScreen(viewModel = viewModel)
                AppScreen.SETTINGS -> SettingsScreen(viewModel = viewModel)
                AppScreen.EXAM_PREVIEW -> ExamPreviewScreen(viewModel = viewModel)
            }

            if (showAiAssistantDialog) {
                AiAssistantDialog(
                    messages = chatMessages,
                    isThinking = isAiThinking,
                    onSendMessage = { prompt -> viewModel.sendAiAssistantQuery(prompt) },
                    onDismiss = { showAiAssistantDialog = false }
                )
            }
        }
    }
}
