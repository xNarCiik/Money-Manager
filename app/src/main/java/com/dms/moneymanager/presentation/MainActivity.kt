package com.dms.moneymanager.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dms.moneymanager.presentation.screen.history.HistoryViewModel
import com.dms.moneymanager.presentation.screen.history.component.HistoryScreen
import com.dms.moneymanager.presentation.screen.main.MainViewModel
import com.dms.moneymanager.presentation.screen.main.component.MainScreen
import com.dms.moneymanager.ui.theme.MoneyManagerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoneyManagerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "main") {
                        composable("main") {
                            val mainViewModel: MainViewModel by viewModels()
                            val viewState = mainViewModel.viewState.collectAsState()
                            MainScreen(
                                viewState = viewState.value,
                                onEvent = mainViewModel::onEvent,
                                navController = navController
                            )
                        }
                        composable("history") {
                            val historyViewModel: HistoryViewModel by viewModels()
                            val viewState = historyViewModel.viewState.collectAsState()
                            HistoryScreen(viewState = viewState.value)
                        }
                    }
                }
            }
        }
    }
}
