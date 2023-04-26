package com.dms.moneymanager.domain.model.main

data class Account(
    val name: String,
    var currentBalance: Float,
    var futureBalance: Float? = null
)