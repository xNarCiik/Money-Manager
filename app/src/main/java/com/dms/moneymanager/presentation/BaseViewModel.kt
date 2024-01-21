package com.dms.moneymanager.presentation

import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

interface BottomSheetType

interface BaseEvent {
    class NavigateToScreen(val route: String) : BaseEvent
    object GoBack : BaseEvent
    object ResetNavigateScreen : BaseEvent
    class OpenBottomSheet(val bottomSheetType: BottomSheetType) : BaseEvent
    object CloseBottomSheet : BaseEvent
    object RemoveToast : BaseEvent
    object OnSnackbarDismissed : BaseEvent
}

data class SnackbarState(
    val message: String,
    val actionLabel: String,
    val duration: SnackbarDuration = SnackbarDuration.Short,
    val onActionPerformed: () -> Unit
)


interface NavigationEvent {
    object GoBack : NavigationEvent
    class NavigateTo(val route: String) : NavigationEvent
}

abstract class BaseViewModel : ViewModel() {
    private var _eventNavigation = MutableStateFlow<NavigationEvent?>(value = null)
    val eventNavigation: StateFlow<NavigationEvent?> = _eventNavigation

    protected var _currentBottomSheet = MutableStateFlow<BottomSheetType?>(value = null)
    val currentBottomSheet: StateFlow<BottomSheetType?> = _currentBottomSheet

    protected var _toastMessage = MutableStateFlow<Int?>(value = null)
    val toastMessage: StateFlow<Int?> = _toastMessage

    protected var _snackbarState = MutableStateFlow<SnackbarState?>(value = null)
    val snackbarState: StateFlow<SnackbarState?> = _snackbarState

    open fun onEvent(event: BaseEvent) {
        viewModelScope.launch {
            when (event) {
                is BaseEvent.GoBack -> {
                    _eventNavigation.value = NavigationEvent.GoBack
                }

                is BaseEvent.NavigateToScreen -> {
                    _eventNavigation.value = NavigationEvent.NavigateTo(route = event.route)
                }

                is BaseEvent.ResetNavigateScreen -> {
                    _eventNavigation.value = null
                }

                is BaseEvent.OpenBottomSheet -> {
                    _currentBottomSheet.value = event.bottomSheetType
                }

                is BaseEvent.CloseBottomSheet -> {
                    _currentBottomSheet.value = null
                }

                is BaseEvent.RemoveToast -> {
                    _toastMessage.value = null
                }

                is BaseEvent.OnSnackbarDismissed -> {
                    _snackbarState.value = null
                }
            }
        }
    }
}