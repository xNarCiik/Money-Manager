package com.dms.moneymanager.presentation.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dms.moneymanager.R
import com.dms.moneymanager.domain.model.main.Account
import com.dms.moneymanager.domain.model.main.Transaction
import com.dms.moneymanager.domain.usecase.AccountUseCase
import com.dms.moneymanager.domain.usecase.TransactionUseCase
import com.dms.moneymanager.presentation.screen.main.model.MainBottomSheetType
import com.dms.moneymanager.presentation.screen.main.model.MainUiModel
import com.dms.moneymanager.presentation.screen.main.model.MainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface MainEvent {
    class AddAccountEvent(val account: Account) : MainEvent
    class RemoveAccountEvent(val account: Account) : MainEvent
    class AddTransactionEvent(val name: String, val amount: String) : MainEvent
    class OnClickAppliedTransaction(val transaction: Transaction) : MainEvent
    class AppliedTransaction(val account: Account) : MainEvent
    class RemoveTransactionEvent(val transaction: Transaction) : MainEvent
    class OpenBottomSheet(val mainBottomSheetType: MainBottomSheetType) : MainEvent
    object CloseBottomSheet : MainEvent
    object RemoveToast : MainEvent
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val accountUseCase: AccountUseCase,
    private val transactionUseCase: TransactionUseCase
) : ViewModel() {

    private var _mainUiState = MutableStateFlow(value = MainUiState.NORMAL)
    private var _currentBalance = MutableStateFlow(value = 0.0f)
    private val _futureBalance = MutableStateFlow(value = 0.0f)
    private var _listAccount = MutableStateFlow<List<Account>>(value = emptyList())
    private var _listTransaction = MutableStateFlow<List<Transaction>>(value = emptyList())
    private var _mainBottomSheetType = MutableStateFlow<MainBottomSheetType?>(value = null)
    private var _toastMessage = MutableStateFlow<Int?>(value = null)

    private var selectedTransaction: Transaction? = null

    @Suppress("UNCHECKED_CAST")
    val viewState = combine(
        _mainUiState,
        _currentBalance,
        _futureBalance,
        _listAccount,
        _listTransaction,
        _mainBottomSheetType,
        _toastMessage
    ) { params ->
        val mainUiState = params[0] as MainUiState
        val currentBalance = params[1] as Float
        val futureBalance = params[2] as Float
        val listAccount = params[3] as List<Account>
        val listTransaction = params[4] as List<Transaction>
        val mainBottomSheetType = params[5] as MainBottomSheetType?
        val toastMessage = params[6] as Int?

        MainUiModel(
            mainUiState = mainUiState,
            currentBalance = currentBalance,
            futureBalance = futureBalance,
            listAccount = listAccount,
            listTransaction = listTransaction,
            mainBottomSheetType = mainBottomSheetType,
            toastMessage = toastMessage
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, MainUiModel())

    init {
        refreshData()
    }

    fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.AddAccountEvent -> {
                createAccount(account = event.account)
            }

            is MainEvent.RemoveAccountEvent -> {
                removeAccount(account = event.account)
            }

            is MainEvent.AddTransactionEvent -> {
                createTransaction(name = event.name, amount = event.amount)
            }

            is MainEvent.OnClickAppliedTransaction -> {
                _mainUiState.value = MainUiState.APPLIED_TRANSACTION
                selectedTransaction = event.transaction
            }

            is MainEvent.AppliedTransaction -> {
                selectedTransaction?.let {
                    appliedTransaction(account = event.account, transaction = it)
                }
                _mainUiState.value = MainUiState.NORMAL
                selectedTransaction = null
            }

            is MainEvent.RemoveTransactionEvent -> {
                removeTransaction(transaction = event.transaction)
            }

            is MainEvent.OpenBottomSheet -> {
                _mainBottomSheetType.value = event.mainBottomSheetType
            }

            is MainEvent.CloseBottomSheet -> {
                _mainBottomSheetType.value = null
            }

            is MainEvent.RemoveToast -> {
                _toastMessage.value = null
            }
        }
    }

    private fun createAccount(account: Account) {
        viewModelScope.launch {
            kotlin.runCatching { accountUseCase.createAccount(account = account) }
                .onSuccess {
                    onEvent(MainEvent.CloseBottomSheet)
                    refreshData()
                }
                .onFailure { _toastMessage.value = R.string.error_failed_add_account }
        }
    }

    private fun removeAccount(account: Account) {
        viewModelScope.launch {
            kotlin.runCatching { accountUseCase.removeAccount(account = account) }
                .onSuccess { refreshData() }
                .onFailure { _toastMessage.value = R.string.error_failed_remove_account }
        }
    }

    private fun createTransaction(name: String, amount: String) {
        viewModelScope.launch {
            if (name.isEmpty()) {
                _toastMessage.value = R.string.error_incorrect_name
                return@launch
            }
            val amountFloat = amount.toFloatOrNull()
            if (amountFloat == null) {
                _toastMessage.value = R.string.error_incorrect_amount
                return@launch
            }
            val transaction = Transaction(name = name, amount = amountFloat)
            kotlin.runCatching { transactionUseCase.createTransaction(transaction = transaction) }
                .onSuccess {
                    onEvent(MainEvent.CloseBottomSheet)
                    refreshData()
                }
                .onFailure { _toastMessage.value = R.string.error_failed_add_transaction }
        }
    }

    private fun appliedTransaction(account: Account, transaction: Transaction) {
        viewModelScope.launch {
            kotlin.runCatching {
                accountUseCase.appliedTransaction(
                    account = account,
                    transaction = transaction
                )
            }
                .onSuccess { refreshData() }
                .onFailure { _toastMessage.value = R.string.error_failed_applied_transaction }
        }
    }

    private fun removeTransaction(transaction: Transaction) {
        viewModelScope.launch {
            kotlin.runCatching { transactionUseCase.removeTransaction(transaction = transaction) }
                .onSuccess { refreshData() }
                .onFailure { _toastMessage.value = R.string.error_failed_remove_account }
        }
    }

    private fun refreshData() {
        viewModelScope.launch {
            _listAccount.value = accountUseCase.getAccounts()
            _listTransaction.value = transactionUseCase.getNotAppliedTransactions()
            _currentBalance.value = accountUseCase.getCurrentBalance(accounts = _listAccount.value)
            _futureBalance.value = accountUseCase.getFutureBalance(
                accounts = _listAccount.value,
                transactions = _listTransaction.value
            )
        }
    }
}