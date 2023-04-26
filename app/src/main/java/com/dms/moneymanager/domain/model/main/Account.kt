package com.dms.moneymanager.domain.model.main

data class Account(
    val name: String,
    val currentBalance: Float,
    val futureBalance: Float? = null
)