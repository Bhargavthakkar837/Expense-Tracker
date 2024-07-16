package com.example.expensetracker

import java.io.Serializable

data class Expense(val category: String, val accountType: String, val amount: Double) : Serializable
