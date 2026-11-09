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
        get() = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE)
            .format(DateTimeFormatter.ofPattern("yyyy-MM"))
}

class ExpenseRepository(private val dao: ExpenseDao) {
    suspend fun getAll(): List<Expense> = dao.getAll().map { it.toDomain() }

    suspend fun saveExpense(expense: Expense) {
        dao.insert(expense.toEntity())
    }

    suspend fun deleteExpense(id: Long) {
        dao.deleteById(id)
    }

    suspend fun getBudget(month: String): Double = dao.getBudget(month)?.amount ?: 0.0

    suspend fun saveBudget(month: String, amount: Double) {
        dao.saveBudget(MonthlyBudgetEntity(month, amount))
    }

    private fun ExpenseEntity.toDomain() = Expense(id, title, amount, category, date, note)

    private fun Expense.toEntity() = ExpenseEntity(id, title, amount, category, date, note)
}
