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
import com.dms.moneymanager.presentation.screen.history.HistoryScreen
import com.dms.moneymanager.presentation.screen.main.MainViewModel
import com.dms.moneymanager.presentation.screen.main.MainScreen
import com.dms.moneymanager.presentation.util.NavigationRoute
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

                    NavHost(navController = navController, startDestination = NavigationRoute.MAIN.route) {
                        composable(NavigationRoute.MAIN.route) {
                            val mainViewModel: MainViewModel by viewModels()
                            val viewState = mainViewModel.viewState.collectAsState()
                            MainScreen(
                                viewState = viewState.value,
                                onEvent = mainViewModel::onEvent,
                                navController = navController
                            )
                        }
                        composable(NavigationRoute.HISTORY.route) {
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
