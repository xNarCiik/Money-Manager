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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dms.moneymanager.presentation.screen.accounts.AccountsBottomSheetType
import com.dms.moneymanager.presentation.screen.accounts.AccountsScreen
import com.dms.moneymanager.presentation.screen.accounts.AccountsViewModel
import com.dms.moneymanager.presentation.screen.accounts.createoredit.CreateOrEditAccountScreen
import com.dms.moneymanager.presentation.screen.commun.BottomBar
import com.dms.moneymanager.presentation.screen.commun.MenuRoute
import com.dms.moneymanager.presentation.screen.history.HistoryScreen
import com.dms.moneymanager.presentation.screen.history.HistoryViewModel
import com.dms.moneymanager.presentation.screen.settings.SettingsScreen
import com.dms.moneymanager.presentation.screen.settings.SettingsViewModel
import com.dms.moneymanager.presentation.screen.transactions.TransactionsBottomSheetType
import com.dms.moneymanager.presentation.screen.transactions.TransactionsScreen
import com.dms.moneymanager.presentation.screen.transactions.TransactionsViewModel
import com.dms.moneymanager.presentation.screen.transactions.component.bottomsheet.BottomSheetConfirmRemoveAccount
import com.dms.moneymanager.presentation.screen.transactions.component.bottomsheet.BottomSheetConfirmRemoveTransaction
import com.dms.moneymanager.presentation.screen.transactions.component.bottomsheet.BottomSheetTransfer
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
                val snackbarHostState = remember { SnackbarHostState() }

                // Base view model observe
                var currentViewModel: BaseViewModel? by remember { mutableStateOf(null) }
                val eventNavigation = currentViewModel?.eventNavigation?.collectAsState()
                val currentBottomSheet =
                    currentViewModel?.currentBottomSheet?.collectAsState()
                val toastMessage =
                    currentViewModel?.toastMessage?.collectAsState()

                // Navigation handle
                LaunchedEffect(key1 = eventNavigation?.value) {
                    eventNavigation?.value?.let { event ->
                        when (event) {
                            is NavigationEvent.GoBack -> navController.popBackStack()
                            is NavigationEvent.NavigateTo -> navController.navigate(route = event.route)
                        }

                        // After navigate : reset currentRoute to avoid multiple navigation
                        currentViewModel?.onEvent(BaseEvent.ResetNavigateScreen)
                    }
                }

                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) },
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

                                    // TODO
//                                    is AccountsBottomSheetType.BottomSheetTransfer -> {
//                                        BottomSheetTransfer(
//                                            listAccount = viewState.accounts,
//                                            account = bottomSheet.account,
//                                            onEvent = { event -> currentViewModel?.onEvent(event) }
//                                        )
//                                    }

                                    is AccountsBottomSheetType.BottomSheetConfirmRemoveAccount -> {
                                        BottomSheetConfirmRemoveAccount(
                                            account = bottomSheet.account,
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
                            composable(route = NavigationRoute.TRANSACTIONS.route) {
                                val transactionsViewModel: TransactionsViewModel by viewModels()
                                currentViewModel = transactionsViewModel
                                val viewState = transactionsViewModel.viewState.collectAsState()

                                TransactionsScreen(
                                    viewState = viewState.value,
                                    onEvent = transactionsViewModel::onEvent
                                )
                            }

                            composable(
                                route = NavigationRoute.CREATE_OR_EDIT_TRANSACTION.route + "?transactionId={transactionId}",
                                arguments = listOf(
                                    navArgument("transactionId") {
                                        type = NavType.IntType
                                        defaultValue = -1
                                    }
                                )
                            ) { backStackEntry ->
                                val transactionsViewModel: TransactionsViewModel by viewModels()
                                currentViewModel = transactionsViewModel
                                val viewState = transactionsViewModel.viewState.collectAsState()
                                val transactionId =
                                    backStackEntry.arguments?.getInt("transactionId")

                                CreateOrEditTransactionScreen(
                                    viewState = viewState.value,
                                    transaction = transactionsViewModel.getTransactionById(id = transactionId),
                                    onEvent = transactionsViewModel::onEvent
                                )
                            }

                            composable(route = NavigationRoute.ACCOUNTS.route) {
                                val accountsViewModel: AccountsViewModel by viewModels()
                                currentViewModel = accountsViewModel
                                val viewState = accountsViewModel.viewState.collectAsState()

                                AccountsScreen(
                                    viewState = viewState.value,
                                    onEvent = accountsViewModel::onEvent
                                )
                            }

                            composable(
                                route = NavigationRoute.CREATE_OR_EDIT_ACCOUNT.route + "?accountId={accountId}",
                                arguments = listOf(
                                    navArgument("accountId") {
                                        type = NavType.IntType
                                        defaultValue = -1
                                    }
                                )
                            ) { backStackEntry ->
                                val accountsViewModel: AccountsViewModel by viewModels()
                                currentViewModel = accountsViewModel
                                val accountId =
                                    backStackEntry.arguments?.getInt("accountId")

                                CreateOrEditAccountScreen(
                                    account = accountsViewModel.getAccountById(id = accountId),
                                    onEvent = accountsViewModel::onEvent
                                )
                            }

                            composable(route = NavigationRoute.SETTINGS.route) {
                                val settingsViewModel: SettingsViewModel by viewModels()
                                currentViewModel = settingsViewModel

                                SettingsScreen(
                                    onEvent = settingsViewModel::onEvent
                                )
                            }

                            composable(route = NavigationRoute.HISTORY.route) {
                                val historyViewModel: HistoryViewModel by viewModels()
                                val viewState = historyViewModel.viewState.collectAsState()
                                HistoryScreen(viewState = viewState.value)
                            }
                        }
                    }

                    // TODO handle differently
//                    when (viewState.transactionsUiState) {
//                        TransactionsUiState.APPLIED_TRANSACTION -> {
//                            LaunchedEffect(key1 = "snackbar_key", block = {
//                                coroutineScope.launch {
//                                    val snackbarResult = snackbarHostState.showSnackbar(
//                                        message = "Cliquez sur le compte vers lequel appliquer la transaction ${viewState.selectedTransaction?.name}.",
//                                        actionLabel = "Annuler",
//                                        duration = SnackbarDuration.Indefinite
//                                    )
//                                    when (snackbarResult) {
//                                        SnackbarResult.ActionPerformed -> onEvent(BaseEvent.ActionPerformedSnackbar)
//                                        SnackbarResult.Dismissed -> {}
//                                    }
//                                }
//                            })
//                        }
//
//                        else -> {
//                            snackbarHostState.currentSnackbarData?.dismiss()
//                        }
//                    }

                    toastMessage?.value?.let { error ->
                        Toast.makeText(LocalContext.current, error, Toast.LENGTH_SHORT).show()
                        currentViewModel?.onEvent(BaseEvent.RemoveToast)
                    }
                }
            }
        }
    }
}
