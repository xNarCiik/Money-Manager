package com.dms.moneymanager.presentation.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dms.moneymanager.domain.model.main.Account
import com.dms.moneymanager.domain.model.main.Transaction
import com.dms.moneymanager.domain.usecase.AccountUseCase
import com.dms.moneymanager.domain.usecase.TransactionUseCase
import com.dms.moneymanager.presentation.screen.main.model.HeaderBackgroundColor
import com.dms.moneymanager.presentation.screen.main.model.MainBottomSheetType
import com.dms.moneymanager.presentation.screen.main.model.MainUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class MainEvent {
    class AddAccountEvent(val account: Account) : MainEvent()
    class RemoveAccountEvent(val account: Account) : MainEvent()
    class AddTransactionEvent(val transaction: Transaction) : MainEvent()
    class AppliedTransaction(val account: Account, val transaction: Transaction) : MainEvent()
    class RemoveTransactionEvent(val transaction: Transaction) : MainEvent()
    class OpenBottomSheet(val mainBottomSheetType: MainBottomSheetType) : MainEvent()
    object CloseBottomSheet : MainEvent()
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val accountUseCase: AccountUseCase,
    private val transactionUseCase: TransactionUseCase
) : ViewModel() {

    private var _headerBackgroundColor = MutableStateFlow(HeaderBackgroundColor.STABLE_BALANCE)
    private var _currentBalance = MutableStateFlow(0.0f)
    private val _futureBalance = MutableStateFlow(0.0f)
    private var _listAccount = MutableStateFlow<List<Account>>(emptyList())
    private var _listTransaction = MutableStateFlow<List<Transaction>>(emptyList())
    private var _mainBottomSheetType = MutableStateFlow<MainBottomSheetType?>(null)

    @Suppress("UNCHECKED_CAST")
    val viewState = combine(
        _headerBackgroundColor,
        _currentBalance,
        _futureBalance,
        _listAccount,
        _listTransaction,
        _mainBottomSheetType
    ) { params ->

        val headerBackgroundColor = params[0] as HeaderBackgroundColor
        val currentBalance = params[1] as Float
        val futureBalance = params[2] as Float
        val listAccount = params[3] as List<Account>
        val listTransaction = params[4] as List<Transaction>
        val mainBottomSheetType = params[5] as MainBottomSheetType?

        MainUiModel(
            headerBackgroundColor = headerBackgroundColor,
            currentBalance = currentBalance,
            futureBalance = futureBalance,
            listAccount = listAccount,
            listTransaction = listTransaction,
            mainBottomSheetType = mainBottomSheetType
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
                createTransaction(transaction = event.transaction)
            }

            is MainEvent.AppliedTransaction -> {
                appliedTransaction(account = event.account, transaction = event.transaction)
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
        }
    }

    private fun createAccount(account: Account) {
        viewModelScope.launch {
            kotlin.runCatching { accountUseCase.createAccount(account = account) }
                .onSuccess { refreshData() }
                .onFailure { /* TODO Handle failure */ }
        }
    }

    private fun removeAccount(account: Account) {
        viewModelScope.launch {
            kotlin.runCatching { accountUseCase.removeAccount(account = account) }
                .onSuccess { refreshData() }
                .onFailure { /* TODO Handle failure */ }
        }
    }

    private fun createTransaction(transaction: Transaction) {
        viewModelScope.launch {
            kotlin.runCatching { transactionUseCase.createTransaction(transaction = transaction) }
                .onSuccess { refreshData() }
                .onFailure { /* TODO Handle failure */ }
        }
    }

    private fun appliedTransaction(account: Account, transaction: Transaction) {
        viewModelScope.launch {
            kotlin.runCatching { accountUseCase.appliedTransaction(account = account, transaction = transaction) }
                .onSuccess { refreshData() }
                .onFailure { /* TODO Handle failure */ }
        }
    }

    private fun removeTransaction(transaction: Transaction) {
        viewModelScope.launch {
            kotlin.runCatching { transactionUseCase.removeTransaction(transaction = transaction) }
                .onSuccess { refreshData() }
                .onFailure { /* TODO Handle failure */ }
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
            _headerBackgroundColor.value = when {
                _futureBalance.value < 0 -> HeaderBackgroundColor.NEGATIVE_BALANCE
                _futureBalance.value > 0 -> HeaderBackgroundColor.POSITIVE_BALANCE
                else -> HeaderBackgroundColor.STABLE_BALANCE
            }
        }
    }
}