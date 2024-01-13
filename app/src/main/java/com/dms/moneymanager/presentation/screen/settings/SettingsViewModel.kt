package com.dms.moneymanager.presentation.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dms.moneymanager.domain.model.main.Account
import com.dms.moneymanager.domain.model.main.Transaction
import com.dms.moneymanager.domain.usecase.AccountUseCase
import com.dms.moneymanager.domain.usecase.TransactionUseCase
import com.dms.moneymanager.presentation.screen.transactions.model.MainBottomSheetType
import com.dms.moneymanager.presentation.screen.transactions.model.MainUiModel
import com.dms.moneymanager.presentation.screen.transactions.model.MainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed interface SettingsEvent {
    class OnClickAppliedTransaction(val transaction: Transaction) : SettingsEvent
    class AppliedTransaction(val toAccount: Account) : SettingsEvent
    class RemoveTransactionEvent(val transaction: Transaction) : SettingsEvent
    class OpenBottomSheet(val mainBottomSheetType: MainBottomSheetType) : SettingsEvent
    object CloseBottomSheet : SettingsEvent
    object RemoveToast : SettingsEvent
    object CancelSnackbar : SettingsEvent
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val accountUseCase: AccountUseCase,
    private val transactionUseCase: TransactionUseCase
) : ViewModel() {

    private var _mainUiState = MutableStateFlow(value = MainUiState.NORMAL)
    private var _currentBalance = MutableStateFlow(value = 0.0f)
    private val _futureBalance = MutableStateFlow(value = 0.0f)
    private var _listAccount = MutableStateFlow<List<Account>>(value = emptyList())
    private var _listTransaction = MutableStateFlow<List<Transaction>>(value = emptyList())
    private var _selectedAccount = MutableStateFlow<Account?>(value = null)
    private var _selectedTransaction = MutableStateFlow<Transaction?>(value = null)
    private var _mainBottomSheetType = MutableStateFlow<MainBottomSheetType?>(value = null)
    private var _toastMessage = MutableStateFlow<Int?>(value = null)

    @Suppress("UNCHECKED_CAST")
    val viewState = combine(
        _mainUiState,
        _currentBalance,
        _futureBalance,
        _listAccount,
        _listTransaction,
        _selectedAccount,
        _selectedTransaction,
        _mainBottomSheetType,
        _toastMessage
    ) { params ->
        val mainUiState = params[0] as MainUiState
        val currentBalance = params[1] as Float
        val futureBalance = params[2] as Float
        val listAccount = params[3] as List<Account>
        val listTransaction = params[4] as List<Transaction>
        val selectedAccount = params[5] as Account?
        val selectedTransaction = params[6] as Transaction?
        val mainBottomSheetType = params[7] as MainBottomSheetType?
        val toastMessage = params[8] as Int?

        MainUiModel(
            mainUiState = mainUiState,
            currentBalance = currentBalance,
            futureBalance = futureBalance,
            accounts = listAccount,
            transactions = listTransaction,
            selectedAccount = selectedAccount,
            selectedTransaction = selectedTransaction,
            mainBottomSheetType = mainBottomSheetType,
            toastMessage = toastMessage
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, MainUiModel())

    init {

    }

    fun onEvent(event: SettingsEvent) {
        // when (event) {
        //
        // }
    }

}