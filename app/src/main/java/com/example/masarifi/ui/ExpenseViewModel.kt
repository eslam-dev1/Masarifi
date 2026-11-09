package com.example.masarifi.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.masarifi.data.Expense
import com.example.masarifi.data.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class ExpenseUiState(
    val expenses: List<Expense> = emptyList(),
    val totalToday: Double = 0.0,
    val totalThisMonth: Double = 0.0,
    val categoryTotals: Map<String, Double> = emptyMap()
)

class ExpenseViewModel : ViewModel() {
    private val repository = ExpenseRepository()
    private val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
    private val currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"))

    private val _uiState = MutableStateFlow(
        ExpenseUiState(
            expenses = repository.getAll(),
            totalToday = repository.totalFor(today),
            totalThisMonth = repository.totalForMonth(currentMonth),
            categoryTotals = repository.totalsByCategory(currentMonth)
        )
    )
    val uiState: StateFlow<ExpenseUiState> = _uiState.asStateFlow()

    fun addExpense(title: String, amount: String, category: String, date: String, note: String) {
        val parsedAmount = amount.toDoubleOrNull() ?: return
        val expense = Expense(
            id = System.currentTimeMillis(),
            title = title.trim(),
            amount = parsedAmount,
            category = category,
            date = date.ifBlank { LocalDate.now().toString() },
            note = note.trim()
        )

        repository.addExpense(expense)
        refresh()
    }

    fun deleteExpense(id: Long) {
        repository.deleteExpense(id)
        refresh()
    }

    private fun refresh() {
        val currentDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        val currentMonthKey = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"))
        _uiState.value = ExpenseUiState(
            expenses = repository.getAll(),
            totalToday = repository.totalFor(currentDate),
            totalThisMonth = repository.totalForMonth(currentMonthKey),
            categoryTotals = repository.totalsByCategory(currentMonthKey)
        )
    }
}
