package com.dms.moneymanager.presentation.screen.accounts

import androidx.lifecycle.viewModelScope
import com.dms.moneymanager.domain.model.main.Account
import com.dms.moneymanager.domain.usecase.AccountUseCase
import com.dms.moneymanager.domain.usecase.TransactionUseCase
import com.dms.moneymanager.presentation.BaseEvent
import com.dms.moneymanager.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AccountsEvent : BaseEvent {
    object RefreshData : BaseEvent
}

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val accountUseCase: AccountUseCase,
    private val transactionUseCase: TransactionUseCase
) : BaseViewModel() {

    private var _listAccount = MutableStateFlow<List<Account>>(value = emptyList())
    private var _currentBalance = MutableStateFlow(value = 0.0f)
    private val _futureBalance = MutableStateFlow(value = 0.0f)

    @Suppress("UNCHECKED_CAST")
    val viewState = combine(
        _listAccount,
        _currentBalance,
        _futureBalance
    ) { params ->
        val listAccount = params[0] as List<Account>
        val currentBalance = params[1] as Float
        val futureBalance = params[2] as Float

        AccountsUiModel(
            accounts = listAccount,
            currentBalance = currentBalance,
            futureBalance = futureBalance,
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, AccountsUiModel())

    override fun onEvent(event: BaseEvent) {
        super.onEvent(event)

        when (event) {
            is AccountsEvent.RefreshData -> {
                refreshData()
            }
        }
    }

    private fun refreshData() {
        viewModelScope.launch {
            val transactions = transactionUseCase.getNotAppliedTransactions()
            val accounts = accountUseCase.getAccounts()
            _currentBalance.value = accountUseCase.getCurrentBalance(accounts = accounts)
            _futureBalance.value = accountUseCase.getFutureBalance(
                accounts = accounts,
                transactions = transactions
            )
            _listAccount.value = accounts
        }
    }
}