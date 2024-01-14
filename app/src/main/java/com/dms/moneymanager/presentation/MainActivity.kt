package com.dms.moneymanager.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dms.moneymanager.presentation.screen.transactions.TransactionsViewModel
import com.dms.moneymanager.presentation.screen.history.HistoryViewModel
import com.dms.moneymanager.presentation.screen.history.HistoryScreen
import com.dms.moneymanager.presentation.screen.settings.SettingsScreen
import com.dms.moneymanager.presentation.screen.settings.SettingsViewModel
import com.dms.moneymanager.presentation.screen.accounts.AccountsScreen
import com.dms.moneymanager.presentation.screen.accounts.AccountsViewModel
import com.dms.moneymanager.presentation.screen.commun.BottomBar
import com.dms.moneymanager.presentation.screen.commun.MenuRoute
import com.dms.moneymanager.presentation.screen.transactions.TransactionsBottomSheetType
import com.dms.moneymanager.presentation.screen.transactions.TransactionsScreen
import com.dms.moneymanager.presentation.screen.transactions.component.bottomsheet.BottomSheetConfirmRemoveTransaction
import com.dms.moneymanager.presentation.screen.transactions.createoredit.CreateOrEditTransactionScreen
import com.dms.moneymanager.presentation.util.NavigationRoute
import com.dms.moneymanager.ui.theme.MoneyManagerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoneyManagerTheme {
                val navController = rememberNavController()

                var currentViewModel: BaseViewModel? by remember { mutableStateOf(null) }
                val currentRoute = currentViewModel?.currentRoute?.collectAsState()
                val currentBottomSheet =
                    currentViewModel?.currentBottomSheet?.collectAsState()
                val toastMessage =
                    currentViewModel?.toastMessage?.collectAsState()

                LaunchedEffect(key1 = currentRoute?.value) {
                    currentRoute?.value?.let { route ->
                        navController.navigate(route = route)
                        // After navigate : reset currentRoute to avoid multiple navigation
                        currentViewModel?.onEvent(BaseEvent.ResetNavigateScreen)
                    }
                }

                Scaffold(
                    bottomBar = {
                        BottomBar(
                            defaultSelectedItem = MenuRoute.TRANSACTIONS,
                            navController = navController
                        )
                    }
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val bottomSheetState =
                            rememberModalBottomSheetState(skipPartiallyExpanded = true)

                        // Sheet content
                        currentBottomSheet?.value?.let { bottomSheet ->
                            ModalBottomSheet(
                                onDismissRequest = { currentViewModel?.onEvent(BaseEvent.CloseBottomSheet) },
                                sheetState = bottomSheetState
                            ) {
                                when (bottomSheet) {
                                    is TransactionsBottomSheetType.BottomSheetConfirmRemoveTransaction -> {
                                        BottomSheetConfirmRemoveTransaction(
                                            transaction = bottomSheet.transaction,
                                            onEvent = { event -> currentViewModel?.onEvent(event) }
                                        )
                                    }

                                    else -> {}
                                }
                            }
                        }

                        NavHost(
                            navController = navController,
                            startDestination = NavigationRoute.TRANSACTIONS.route
                        ) {
                            composable(NavigationRoute.TRANSACTIONS.route) {
                                val transactionsViewModel: TransactionsViewModel by viewModels()
                                currentViewModel = transactionsViewModel
                                val viewState = transactionsViewModel.viewState.collectAsState()

                                TransactionsScreen(
                                    viewState = viewState.value,
                                    onEvent = transactionsViewModel::onEvent
                                )
                            }

                            composable(NavigationRoute.CREATE_OR_EDIT_TRANSACTION.route) {
                                val transactionsViewModel: TransactionsViewModel by viewModels()
                                currentViewModel = transactionsViewModel
                                val viewState = transactionsViewModel.viewState.collectAsState()

                                CreateOrEditTransactionScreen(
                                    viewState = viewState.value,
                                    onEvent = transactionsViewModel::onEvent
                                )
                            }

                            composable(NavigationRoute.ACCOUNTS.route) {
                                val accountsViewModel: AccountsViewModel by viewModels()
                                currentViewModel = accountsViewModel
                                val viewState = accountsViewModel.viewState.collectAsState()

                                AccountsScreen(
                                    viewState = viewState.value,
                                    onEvent = accountsViewModel::onEvent
                                )
                            }

                            composable(NavigationRoute.SETTINGS.route) {
                                val settingsViewModel: SettingsViewModel by viewModels()
                                currentViewModel = settingsViewModel

                                SettingsScreen(
                                    onEvent = settingsViewModel::onEvent
                                )
                            }

                            composable(NavigationRoute.HISTORY.route) {
                                val historyViewModel: HistoryViewModel by viewModels()
                                val viewState = historyViewModel.viewState.collectAsState()
                                HistoryScreen(viewState = viewState.value)
                            }
                        }
                    }


                    toastMessage?.value?.let { error ->
                        Toast.makeText(LocalContext.current, error, Toast.LENGTH_SHORT).show()
                        currentViewModel?.onEvent(BaseEvent.RemoveToast)
                    }
                }
            }
        }
    }
}
