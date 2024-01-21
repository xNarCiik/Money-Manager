package com.dms.moneymanager.presentation.screen.transactions

import androidx.lifecycle.viewModelScope
import com.dms.moneymanager.R
import com.dms.moneymanager.domain.model.main.Account
import com.dms.moneymanager.domain.model.main.Transaction
import com.dms.moneymanager.domain.usecase.AccountUseCase
import com.dms.moneymanager.domain.usecase.TransactionUseCase
import com.dms.moneymanager.presentation.BaseEvent
import com.dms.moneymanager.presentation.BaseViewModel
import com.dms.moneymanager.presentation.SnackbarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

sealed interface TransactionsEvent : BaseEvent {
    data object RefreshData : TransactionsEvent
    data object ClickLeftArrowDate : TransactionsEvent
    data object ClickRightArrowDate : TransactionsEvent

    class ClickAddTransaction(
        val name: String,
        val amount: String,
        val destinationAccount: Account?
    ) : TransactionsEvent

    class ClickEditTransaction(
        val id: Int,
        val name: String,
        val amount: String,
        val destinationAccount: Account?
    ) : TransactionsEvent

    class ClickEnableOrDisableTransaction(val transaction: Transaction) : TransactionsEvent
    class ClickAppliedTransaction(val transaction: Transaction) : TransactionsEvent
    class AppliedTransaction(val toAccount: Account) : TransactionsEvent
    class ClickRemoveTransaction(val transaction: Transaction) : TransactionsEvent
    class ClickCancelRemoveTransaction(val transaction: Transaction) : TransactionsEvent
}

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val accountUseCase: AccountUseCase,
    private val transactionUseCase: TransactionUseCase
) : BaseViewModel() {

    private var calendar = Calendar.getInstance()

    private var _currentDate = MutableStateFlow<Date>(value = calendar.time)
    private var _listAccount = MutableStateFlow<List<Account>>(value = emptyList())
    private var _listTransaction = MutableStateFlow<List<Transaction>>(value = emptyList())
    private var _selectedAccount = MutableStateFlow<Account?>(value = null)
    private var _selectedTransaction = MutableStateFlow<Transaction?>(value = null)

    @Suppress("UNCHECKED_CAST")
    val viewState = combine(
        _currentDate,
        _listAccount,
        _listTransaction,
        _selectedAccount,
        _selectedTransaction,
    ) { params ->
        val currentDate = params[0] as Date
        val listAccount = params[1] as List<Account>
        val listTransaction = params[2] as List<Transaction>
        val selectedAccount = params[3] as Account?
        val selectedTransaction = params[4] as Transaction?

        TransactionsUiModel(
            currentDate = currentDate,
            accounts = listAccount,
            transactions = listTransaction,
            selectedAccount = selectedAccount,
            selectedTransaction = selectedTransaction,
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, TransactionsUiModel())

    init {
        onEvent(event = TransactionsEvent.RefreshData)
    }

    override fun onEvent(event: BaseEvent) {
        super.onEvent(event)

        when (event) {
            is TransactionsEvent.RefreshData -> {
                refreshData()
            }

            is TransactionsEvent.ClickLeftArrowDate -> {
                calendar.add(Calendar.MONTH, -1)
                _currentDate.value = calendar.time
            }

            is TransactionsEvent.ClickRightArrowDate -> {
                calendar.add(Calendar.MONTH, 1)
                _currentDate.value = calendar.time
            }

            is TransactionsEvent.ClickAddTransaction -> {
                createTransaction(
                    name = event.name,
                    amount = event.amount,
                    destinationAccount = event.destinationAccount
                )
            }

            is TransactionsEvent.ClickEditTransaction -> {
                editTransaction(
                    id = event.id,
                    name = event.name,
                    amount = event.amount,
                    destinationAccount = event.destinationAccount
                )
            }

            is TransactionsEvent.ClickEnableOrDisableTransaction -> {
                viewModelScope.launch {
                    transactionUseCase.enableOrDisableTransaction(transaction = event.transaction)
                    refreshData()
                }
            }

            // is TransactionsEvent.OnClickAppliedTransaction -> {
            //     // TODO Handle differently
            //     _transactionsUiState.value = TransactionsUiState.APPLIED_TRANSACTION
            //     _selectedTransaction.value = event.transaction
            // }
            //
            // is TransactionsEvent.AppliedTransaction -> {
            //     // TODO Handle differently
            //     _selectedTransaction.value?.let {
            //         appliedTransaction(account = event.toAccount, transaction = it)
            //     }
            //     onEvent(event = BaseEvent.ActionPerformedSnackbar)
            // }

            is TransactionsEvent.ClickRemoveTransaction -> {
                val transaction = event.transaction
                _snackbarState.value = SnackbarState(
                    message = "Transaction ${transaction.name} supprimé.",
                    actionLabel = "Annuler",
                    onActionPerformed = { onEvent(event = TransactionsEvent.ClickCancelRemoveTransaction(transaction = transaction))}
                )
                removeTransaction(transaction = transaction)
            }

            is TransactionsEvent.ClickCancelRemoveTransaction -> {
                viewModelScope.launch {
                    kotlin.runCatching { transactionUseCase.createTransaction(transaction = event.transaction) }.onSuccess {
                        _snackbarState.value = null
                        refreshData()
                    }
                }
            }
        }
    }

    fun getTransactionById(id: Int?) =
        _listTransaction.value.find { it.id == id }

    private fun refreshData() {
        viewModelScope.launch {
            _listTransaction.value = transactionUseCase.getNotAppliedTransactions()
            _listAccount.value = accountUseCase.getAccounts()
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
                    onEvent(BaseEvent.GoBack)
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
                    onEvent(BaseEvent.GoBack)
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
}