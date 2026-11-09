package com.example.masarifi.data

import java.time.LocalDate
import java.time.format.DateTimeFormatter

/** Domain model used by the UI. */
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

    suspend fun addExpense(expense: Expense) {
        dao.insert(expense.toEntity())
    }

    suspend fun deleteExpense(id: Long) {
        dao.deleteById(id)
    }

    private fun ExpenseEntity.toDomain() = Expense(
        id = id,
        title = title,
        amount = amount,
        category = category,
        date = date,
        note = note
    )

    private fun Expense.toEntity() = ExpenseEntity(
        id = id,
        title = title,
        amount = amount,
        category = category,
        date = date,
        note = note
    )
}
