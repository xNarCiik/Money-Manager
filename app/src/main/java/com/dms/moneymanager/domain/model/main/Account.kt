package com.dms.moneymanager.domain.model.main

data class Account(
    val id: Int = 0,
    val name: String,
    var currentBalance: Float,
    var futureBalance: Float? = null,
    var hasOverdraft: Boolean = false,
    var overdraftLimit: Float? = null
)