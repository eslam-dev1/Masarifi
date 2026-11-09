package com.example.masarifi.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.masarifi.data.Expense
import com.example.masarifi.data.ExpenseRepository
import com.example.masarifi.data.MasarifiDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class ExpenseUiState(
    val expenses: List<Expense> = emptyList(),
    val totalToday: Double = 0.0,
    val totalThisMonth: Double = 0.0,
    val categoryTotals: Map<String, Double> = emptyMap()
)

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ExpenseRepository(
        MasarifiDatabase.getInstance(application).expenseDao()
    )

    private val _uiState = MutableStateFlow(ExpenseUiState())
    val uiState: StateFlow<ExpenseUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun addExpense(title: String, amount: String, category: String, date: String, note: String) {
        val parsedAmount = amount.toDoubleOrNull()?.takeIf { it > 0 } ?: return
        val expense = Expense(
            id = System.currentTimeMillis(),
            title = title.trim(),
            amount = parsedAmount,
            category = category,
            date = date.ifBlank { LocalDate.now().toString() },
            note = note.trim()
        )

        viewModelScope.launch {
            repository.addExpense(expense)
            refresh()
        }
    }

    fun deleteExpense(id: Long) {
        viewModelScope.launch {
            repository.deleteExpense(id)
            refresh()
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            val expenses = repository.getAll()
            val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
            val currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"))
            val thisMonth = expenses.filter { it.monthKey == currentMonth }

            _uiState.value = ExpenseUiState(
                expenses = expenses,
                totalToday = expenses.filter { it.date == today }.sumOf { it.amount },
                totalThisMonth = thisMonth.sumOf { it.amount },
                categoryTotals = thisMonth.groupBy { it.category }
                    .mapValues { (_, items) -> items.sumOf { it.amount } }
                    .toSortedMap()
            )
        }
    }
}
