package com.example.masarifi.data

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class Expense(
    val id: Long,
    val title: String,
    val amount: Double,
    val category: String,
    val date: String,
    val note: String = ""
) {
    val monthKey: String
        get() = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE).format(DateTimeFormatter.ofPattern("yyyy-MM"))
}

class ExpenseRepository {
    private val expenses = mutableListOf(
        Expense(
            id = 1L,
            title = "أكل", amount = 55.0,
            category = "طعام",
            date = LocalDate.now().toString(),
            note = "غداء"),
        Expense(
            id = 2L,
            title = "بنزين", amount = 120.0,
            category = "نقل",
            date = LocalDate.now().toString(),
            note = "دفع وقود"),
        Expense(
            id = 3L,
            title = "فاتورة إنترنت", amount = 90.0,
            category = "فواتير",
            date = LocalDate.now().minusDays(5).toString(),
            note = "شهرية"),
    )

    fun getAll(): List<Expense> = expenses.sortedByDescending { it.date }

    fun addExpense(expense: Expense) {
        expenses.add(0, expense)
    }

    fun deleteExpense(id: Long) {
        expenses.removeAll { it.id == id }
    }

    fun totalFor(date: String): Double = expenses.filter { it.date == date }.sumOf { it.amount }

    fun totalForMonth(month: String): Double = expenses.filter { it.monthKey == month }.sumOf { it.amount }

    fun totalsByCategory(month: String): Map<String, Double> = expenses
        .filter { it.monthKey == month }
        .groupBy { it.category }
        .mapValues { (_, list) -> list.sumOf { it.amount } }
        .toSortedMap()
}
