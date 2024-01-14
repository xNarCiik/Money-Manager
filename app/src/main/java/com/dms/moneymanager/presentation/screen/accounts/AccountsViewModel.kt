package com.dms.moneymanager.presentation.screen.accounts

import androidx.lifecycle.viewModelScope
import com.dms.moneymanager.domain.model.main.Account
import com.dms.moneymanager.domain.usecase.AccountUseCase
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
    private val accountUseCase: AccountUseCase
) : BaseViewModel() {

    private var _listAccount = MutableStateFlow<List<Account>>(value = emptyList())

    @Suppress("UNCHECKED_CAST")
    val viewState = combine(
        _listAccount
    ) { params ->
        val listAccount = params[0] as List<Account>

        AccountsUiModel(
            accounts = listAccount
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
            _listAccount.value = accountUseCase.getAccounts()
        }
    }
}