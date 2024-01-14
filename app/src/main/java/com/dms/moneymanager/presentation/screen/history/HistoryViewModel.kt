package com.dms.moneymanager.presentation.screen.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dms.moneymanager.domain.usecase.TransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val transactionUseCase: TransactionUseCase
) : ViewModel() {

    private var _viewState = MutableStateFlow(HistoryUiModel())
    val viewState = _viewState as StateFlow<HistoryUiModel>

    init {
        viewModelScope.launch {
            _viewState.value =
                _viewState.value.copy(listTransaction = transactionUseCase.getAllTransactions())
        }
    }
}
