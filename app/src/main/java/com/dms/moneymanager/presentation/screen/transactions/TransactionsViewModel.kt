package com.dms.moneymanager.presentation.screen.transactions

import androidx.lifecycle.viewModelScope
import com.dms.moneymanager.R
import com.dms.moneymanager.domain.model.main.Account
import com.dms.moneymanager.domain.model.main.Transaction
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

sealed interface TransactionsEvent : BaseEvent {
    object RefreshData : TransactionsEvent
    class AddAccountEvent(val name: String, val balance: String) : TransactionsEvent
    class EditAccountEvent(val id: Int, val name: String, val balance: String) : TransactionsEvent
    class EnableOrDisableAccountEvent(val account: Account) : TransactionsEvent
    class RemoveAccountEvent(val account: Account) : TransactionsEvent
    class OnClickTransfer(
        val transmitterAccount: Account,
        val receiverAccount: Account?,
        val amount: String
    ) : TransactionsEvent

    class AddTransactionEvent(
        val name: String,
        val amount: String,
        val destinationAccount: Account?
    ) : TransactionsEvent

    class EditTransactionEvent(
        val id: Int,
        val name: String,
        val amount: String,
        val destinationAccount: Account?
    ) : TransactionsEvent

    class EnableOrDisableTransactionEvent(val transaction: Transaction) : TransactionsEvent
    class OnClickAppliedTransaction(val transaction: Transaction) : TransactionsEvent
    class AppliedTransaction(val toAccount: Account) : TransactionsEvent
    class RemoveTransactionEvent(val transaction: Transaction) : TransactionsEvent
}

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val accountUseCase: AccountUseCase,
    private val transactionUseCase: TransactionUseCase
) : BaseViewModel() {

    private var _transactionsUiState = MutableStateFlow(value = TransactionsUiState.NORMAL)
    private var _currentBalance = MutableStateFlow(value = 0.0f)
    private val _futureBalance = MutableStateFlow(value = 0.0f)
    private var _listAccount = MutableStateFlow<List<Account>>(value = emptyList())
    private var _listTransaction = MutableStateFlow<List<Transaction>>(value = emptyList())
    private var _selectedAccount = MutableStateFlow<Account?>(value = null)
    private var _selectedTransaction = MutableStateFlow<Transaction?>(value = null)

    @Suppress("UNCHECKED_CAST")
    val viewState = combine(
        _transactionsUiState,
        _currentBalance,
        _futureBalance,
        _listAccount,
        _listTransaction,
        _selectedAccount,
        _selectedTransaction,
    ) { params ->
        val transactionsUiState = params[0] as TransactionsUiState
        val currentBalance = params[1] as Float
        val futureBalance = params[2] as Float
        val listAccount = params[3] as List<Account>
        val listTransaction = params[4] as List<Transaction>
        val selectedAccount = params[5] as Account?
        val selectedTransaction = params[6] as Transaction?

        TransactionsUiModel(
            transactionsUiState = transactionsUiState,
            currentBalance = currentBalance,
            futureBalance = futureBalance,
            accounts = listAccount,
            transactions = listTransaction,
            selectedAccount = selectedAccount,
            selectedTransaction = selectedTransaction,
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, TransactionsUiModel())

    override fun onEvent(event: BaseEvent) {
        super.onEvent(event)

        when (event) {
            is TransactionsEvent.RefreshData -> {
                refreshData()
            }

            is TransactionsEvent.AddAccountEvent -> {
                createAccount(name = event.name, balance = event.balance)
            }

            is TransactionsEvent.EditAccountEvent -> {
                editAccount(id = event.id, name = event.name, balance = event.balance)
            }

            is TransactionsEvent.EnableOrDisableAccountEvent -> {
                viewModelScope.launch {
                    accountUseCase.enableOrDisableAccount(account = event.account)
                    refreshData()
                }
            }

            is TransactionsEvent.RemoveAccountEvent -> {
                removeAccount(account = event.account)
                _currentBottomSheet.value = null
            }

            is TransactionsEvent.OnClickTransfer -> {
                transfer(
                    transmitterAccount = event.transmitterAccount,
                    receiverAccount = event.receiverAccount,
                    amount = event.amount
                )
            }

            is TransactionsEvent.AddTransactionEvent -> {
                createTransaction(
                    name = event.name,
                    amount = event.amount,
                    destinationAccount = event.destinationAccount
                )
            }

            is TransactionsEvent.EditTransactionEvent -> {
                editTransaction(
                    id = event.id,
                    name = event.name,
                    amount = event.amount,
                    destinationAccount = event.destinationAccount
                )
            }

            is TransactionsEvent.EnableOrDisableTransactionEvent -> {
                viewModelScope.launch {
                    transactionUseCase.enableOrDisableTransaction(transaction = event.transaction)
                    refreshData()
                }
            }

            is TransactionsEvent.OnClickAppliedTransaction -> {
                _transactionsUiState.value = TransactionsUiState.APPLIED_TRANSACTION
                _selectedTransaction.value = event.transaction
            }

            is TransactionsEvent.AppliedTransaction -> {
                _selectedTransaction.value?.let {
                    appliedTransaction(account = event.toAccount, transaction = it)
                }
                onEvent(BaseEvent.ActionPerformedSnackbar)
            }

            is TransactionsEvent.RemoveTransactionEvent -> {
                removeTransaction(transaction = event.transaction)
                _currentBottomSheet.value = null
            }

            is BaseEvent.ActionPerformedSnackbar -> {
                when (_transactionsUiState.value) {
                    TransactionsUiState.APPLIED_TRANSACTION -> {
                        _transactionsUiState.value = TransactionsUiState.NORMAL
                        _selectedTransaction.value = null
                    }

                    else -> {}
                }
            }
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
                    onEvent(BaseEvent.CloseBottomSheet)
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
                    onEvent(BaseEvent.CloseBottomSheet)
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

    private fun createTransaction(name: String, amount: String, destinationAccount: Account?) {
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
            val transaction = Transaction(
                name = name,
                amount = amountFloat,
                destinationAccount = destinationAccount
            )
            kotlin.runCatching { transactionUseCase.createTransaction(transaction = transaction) }
                .onSuccess {
                    onEvent(BaseEvent.CloseBottomSheet)
                    refreshData()
                }
                .onFailure { _toastMessage.value = R.string.error_failed_add_transaction }
        }
    }

    private fun editTransaction(
        id: Int,
        name: String,
        amount: String,
        destinationAccount: Account?
    ) {
        viewModelScope.launch {
            // TODO REFACTO CHECK WITH CREATE
            if (name.isEmpty()) {
                _toastMessage.value = R.string.error_incorrect_name
                return@launch
            }
            val amountFloat = amount.toFloatOrNull()
            if (amountFloat == null) {
                _toastMessage.value = R.string.error_incorrect_balance
                return@launch
            }
            kotlin.runCatching {
                transactionUseCase.updateTransaction(
                    id = id,
                    name = name,
                    amount = amountFloat,
                    destinationAccount = destinationAccount
                )
            }
                .onSuccess {
                    onEvent(BaseEvent.CloseBottomSheet)
                    refreshData()
                }
                .onFailure { _toastMessage.value = R.string.error_failed_add_account }
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
            _listTransaction.value = transactionUseCase.getNotAppliedTransactions()
            val accounts = accountUseCase.getAccounts()
            _currentBalance.value = accountUseCase.getCurrentBalance(accounts = accounts)
            _futureBalance.value = accountUseCase.getFutureBalance(
                accounts = accounts,
                transactions = _listTransaction.value
            )
            _listAccount.value = accounts
        }
    }
}