package com.dms.moneymanager.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface BottomSheetType

interface BaseEvent {
    class NavigateToScreen(val route: String) : BaseEvent
    object ResetNavigateScreen : BaseEvent
    class OpenBottomSheet(val bottomSheetType: BottomSheetType) : BaseEvent
    object CloseBottomSheet : BaseEvent
    object RemoveToast : BaseEvent
    object ActionPerformedSnackbar : BaseEvent
}

abstract class BaseViewModel : ViewModel() {
    protected var _currentRoute = MutableStateFlow<String?>(value = null)
    val currentRoute: StateFlow<String?> = _currentRoute

    protected var _currentBottomSheet = MutableStateFlow<BottomSheetType?>(value = null)
    val currentBottomSheet: StateFlow<BottomSheetType?> = _currentBottomSheet

    protected var _toastMessage = MutableStateFlow<Int?>(value = null)
    val toastMessage: StateFlow<Int?> = _toastMessage

    open fun onEvent(event: BaseEvent) {
        when (event) {
            is BaseEvent.NavigateToScreen -> {
                _currentRoute.value = event.route
            }

            is BaseEvent.ResetNavigateScreen -> {
                _currentRoute.value = null
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
        }
    }
}