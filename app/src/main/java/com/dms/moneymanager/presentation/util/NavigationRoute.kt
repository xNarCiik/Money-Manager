package com.dms.moneymanager.presentation.util

enum class NavigationRoute(val route: String) {
    TRANSACTIONS(route = "transactions"),
    CREATE_OR_EDIT_TRANSACTION(route = "create_or_edit_transaction"),
    ACCOUNTS(route = "accounts"),
    CREATE_OR_EDIT_ACCOUNT(route = "create_or_edit_account"),
    SETTINGS(route = "settings"),
    HISTORY(route = "history")
}