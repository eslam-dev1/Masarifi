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
    val categoryTotals: Map<String, Double> = emptyMap(),
    val monthlyBudget: Double = 0.0,
    val isLoading: Boolean = true
)

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ExpenseRepository(MasarifiDatabase.getInstance(application).expenseDao())
    private val _uiState = MutableStateFlow(ExpenseUiState())
    val uiState: StateFlow<ExpenseUiState> = _uiState.asStateFlow()

    init { refresh() }

    fun saveExpense(title: String, amount: String, category: String, date: String, note: String, id: Long? = null) {
        val parsedAmount = amount.toDoubleOrNull()?.takeIf { it > 0 } ?: return
        val validDate = runCatching { LocalDate.parse(date); true }.getOrDefault(false)
        if (title.isBlank() || !validDate) return
        viewModelScope.launch {
            repository.saveExpense(Expense(id ?: System.currentTimeMillis(), title.trim(), parsedAmount, category, date, note.trim()))
            refresh()
        }
    }

    fun deleteExpense(id: Long) {
        viewModelScope.launch { repository.deleteExpense(id); refresh() }
    }

    fun saveMonthlyBudget(amount: String) {
        val parsedAmount = amount.toDoubleOrNull()?.takeIf { it >= 0 } ?: return
        viewModelScope.launch {
            repository.saveBudget(currentMonth(), parsedAmount)
            refresh()
        }
    }

    private fun currentMonth() = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"))

    private fun refresh() {
        viewModelScope.launch {
            val expenses = repository.getAll()
            val today = LocalDate.now().toString()
            val month = currentMonth()
            val thisMonth = expenses.filter { it.monthKey == month }
            _uiState.value = ExpenseUiState(
                expenses = expenses,
                totalToday = expenses.filter { it.date == today }.sumOf { it.amount },
                totalThisMonth = thisMonth.sumOf { it.amount },
                categoryTotals = thisMonth.groupBy { it.category }
                    .mapValues { (_, entries) -> entries.sumOf { it.amount } }
                    .toSortedMap(),
                monthlyBudget = repository.getBudget(month),
                isLoading = false
            )
        }
    }
}
