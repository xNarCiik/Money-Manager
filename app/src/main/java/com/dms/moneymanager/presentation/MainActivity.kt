package com.dms.moneymanager.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dms.moneymanager.presentation.screen.history.HistoryViewModel
import com.dms.moneymanager.presentation.screen.history.HistoryScreen
import com.dms.moneymanager.presentation.screen.MainViewModel
import com.dms.moneymanager.presentation.screen.settings.SettingsScreen
import com.dms.moneymanager.presentation.screen.settings.SettingsViewModel
import com.dms.moneymanager.presentation.screen.accounts.AccountsScreen
import com.dms.moneymanager.presentation.screen.commun.BottomBar
import com.dms.moneymanager.presentation.screen.commun.MenuRoute
import com.dms.moneymanager.presentation.screen.transactions.TransactionsScreen
import com.dms.moneymanager.presentation.util.NavigationRoute
import com.dms.moneymanager.ui.theme.MoneyManagerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoneyManagerTheme {
                val navController = rememberNavController()

                Scaffold(
                    bottomBar = {
                        BottomBar(
                            defaultSelectedItem = MenuRoute.TRANSACTIONS,
                            navController = navController
                        )
                    }
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize().padding(it),
                        color = MaterialTheme.colorScheme.background
                    ) {

                        NavHost(
                            navController = navController,
                            startDestination = NavigationRoute.TRANSACTIONS.route
                        ) {
                            composable(NavigationRoute.TRANSACTIONS.route) {
                                val mainViewModel: MainViewModel by viewModels()
                                val viewState = mainViewModel.viewState.collectAsState()

                                TransactionsScreen(
                                    viewState = viewState.value,
                                    onEvent = mainViewModel::onEvent,
                                    navController = navController
                                )
                            }

                            composable(NavigationRoute.ACCOUNTS.route) {
                                AccountsScreen(
                                    navController = navController
                                )
                            }

                            composable(NavigationRoute.SETTINGS.route) {
                                val settingsViewModel: SettingsViewModel by viewModels()
                                SettingsScreen(
                                    onEvent = settingsViewModel::onEvent,
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
}
