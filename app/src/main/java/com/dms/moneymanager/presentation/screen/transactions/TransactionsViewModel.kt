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
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

sealed interface TransactionsEvent : BaseEvent {
    data object RefreshData : TransactionsEvent
    data object OnClickLeftArrowDate: TransactionsEvent
    data object OnClickRightArrowDate: TransactionsEvent

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

    private var calendar = Calendar.getInstance()

    private var _transactionsUiState = MutableStateFlow(value = TransactionsUiState.NORMAL)
    private var _currentDate = MutableStateFlow<Date>(value = calendar.time)
    private var _listAccount = MutableStateFlow<List<Account>>(value = emptyList())
    private var _listTransaction = MutableStateFlow<List<Transaction>>(value = emptyList())
    private var _selectedAccount = MutableStateFlow<Account?>(value = null)
    private var _selectedTransaction = MutableStateFlow<Transaction?>(value = null)

    @Suppress("UNCHECKED_CAST")
    val viewState = combine(
        _transactionsUiState,
        _currentDate,
        _listAccount,
        _listTransaction,
        _selectedAccount,
        _selectedTransaction,
    ) { params ->
        val transactionsUiState = params[0] as TransactionsUiState
        val currentDate = params[1] as Date
        val listAccount = params[2] as List<Account>
        val listTransaction = params[3] as List<Transaction>
        val selectedAccount = params[4] as Account?
        val selectedTransaction = params[5] as Transaction?

        TransactionsUiModel(
            transactionsUiState = transactionsUiState,
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

            is TransactionsEvent.OnClickLeftArrowDate -> {
                calendar.add(Calendar.MONTH, -1)
                _currentDate.value = calendar.time
            }

            is TransactionsEvent.OnClickRightArrowDate -> {
                calendar.add(Calendar.MONTH, 1)
                _currentDate.value = calendar.time
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
                // TODO Handle differently
                _transactionsUiState.value = TransactionsUiState.APPLIED_TRANSACTION
                _selectedTransaction.value = event.transaction
            }

            is TransactionsEvent.AppliedTransaction -> {
                // TODO Handle differently
                _selectedTransaction.value?.let {
                    appliedTransaction(account = event.toAccount, transaction = it)
                }
                onEvent(event = BaseEvent.ActionPerformedSnackbar)
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