package com.dms.moneymanager.presentation.screen.accounts

import androidx.lifecycle.viewModelScope
import com.dms.moneymanager.R
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
    class AddAccountEvent(val name: String, val balance: String) : AccountsEvent
    class EditAccountEvent(val id: Int, val name: String, val balance: String) : AccountsEvent
    class EnableOrDisableAccountEvent(val account: Account) : AccountsEvent
    class RemoveAccountEvent(val account: Account) : AccountsEvent
    class OnClickTransfer(
        val transmitterAccount: Account,
        val receiverAccount: Account?,
        val amount: String
    ) : AccountsEvent
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

            is AccountsEvent.AddAccountEvent -> {
                createAccount(name = event.name, balance = event.balance)
            }

            is AccountsEvent.EditAccountEvent -> {
                editAccount(id = event.id, name = event.name, balance = event.balance)
            }

            is AccountsEvent.EnableOrDisableAccountEvent -> {
                viewModelScope.launch {
                    accountUseCase.enableOrDisableAccount(account = event.account)
                    refreshData()
                }
            }

            is AccountsEvent.RemoveAccountEvent -> {
                removeAccount(account = event.account)
                _currentBottomSheet.value = null
            }

            is AccountsEvent.OnClickTransfer -> {
                transfer(
                    transmitterAccount = event.transmitterAccount,
                    receiverAccount = event.receiverAccount,
                    amount = event.amount
                )
            }
        }
    }

    fun getAccountById(id: Int?) = _listAccount.value.find { it.id == id }

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

    private fun createAccount(name: String, balance: String) {
        viewModelScope.launch {
            if (name.isEmpty()) {
                _toastMessage.value = R.string.error_incorrect_name
                return@launch
            }
            val balanceFloat = balance.toFloatOrNull()
            if (balanceFloat == null) {
                _toastMessage.value = R.string.error_incorrect_balance
                return@launch
            }
            val account = Account(name = name, currentBalance = balanceFloat)
            kotlin.runCatching { accountUseCase.createAccount(account = account) }
                .onSuccess {
                    onEvent(BaseEvent.GoBack)
                    refreshData()
                }
                .onFailure { _toastMessage.value = R.string.error_failed_add_account }
        }
    }

    private fun editAccount(id: Int, name: String, balance: String) {
        viewModelScope.launch {
            // TODO REFACTO CHECK WITH CREATE
            if (name.isEmpty()) {
                _toastMessage.value = R.string.error_incorrect_name
                return@launch
            }
            val balanceFloat = balance.toFloatOrNull()
            if (balanceFloat == null) {
                _toastMessage.value = R.string.error_incorrect_balance
                return@launch
            }
            kotlin.runCatching {
                accountUseCase.updateAccount(
                    id = id,
                    name = name,
                    currentBalance = balanceFloat
                )
            }
                .onSuccess {
                    onEvent(BaseEvent.GoBack)
                    refreshData()
                }
                .onFailure { _toastMessage.value = R.string.error_failed_add_account }
        }
    }

    private fun transfer(transmitterAccount: Account, receiverAccount: Account?, amount: String) {
        viewModelScope.launch {
            val amountFloat = amount.toFloatOrNull()
            if (amountFloat == null || transmitterAccount.currentBalance < amountFloat || amountFloat <= 0.0f) {
                _toastMessage.value = R.string.error_incorrect_amount
                return@launch
            }
            if (receiverAccount == null) {
                _toastMessage.value = R.string.error_account_not_selected
                return@launch
            }
            kotlin.runCatching {
                accountUseCase.transfer(
                    transmitterAccount = transmitterAccount,
                    receiverAccount = receiverAccount,
                    amount = amountFloat
                )
            }
                .onSuccess {
                    onEvent(BaseEvent.CloseBottomSheet)
                    refreshData()
                }
                .onFailure { _toastMessage.value = R.string.error_failed_transfer }
        }
    }

    private fun removeAccount(account: Account) {
        viewModelScope.launch {
            kotlin.runCatching { accountUseCase.removeAccount(account = account) }
                .onSuccess { refreshData() }
                .onFailure { _toastMessage.value = R.string.error_failed_remove_account }
        }
    }
}