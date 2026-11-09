package com.example.masarifi.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.masarifi.data.Expense
import java.text.NumberFormat
import java.util.Locale

@Composable
fun MasarifiApp(viewModel: ExpenseViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("مصاريفي") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "إضافة مصروف")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            SummaryCards(
                today = state.totalToday,
                month = state.totalThisMonth,
                categories = state.categoryTotals
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "آخر المصروفات",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.expenses) { expense ->
                    ExpenseItem(
                        expense = expense,
                        onDelete = { viewModel.deleteExpense(expense.id) }
                    )
                }
            }
        }
    }

    if (showDialog) {
        AddExpenseDialog(
            onDismiss = { showDialog = false },
            onSubmit = { title, amount, category, date, note ->
                viewModel.addExpense(title, amount, category, date, note)
                showDialog = false
            }
        )
    }
}

@Composable
private fun SummaryCards(
    today: Double,
    month: Double,
    categories: Map<String, Double>
) {
    val currency = NumberFormat.getNumberInstance(Locale("ar", "SA")).apply {
        maximumFractionDigits = 2
    }

    val topCategory = categories.entries.maxByOrNull { it.value }

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        SummaryCard(
            title = "اليوم",
            value = "${currency.format(today)} ر.س",
            color = Color(0xFF4CAF50)
        )
        SummaryCard(
            title = "هذا الشهر",
            value = "${currency.format(month)} ر.س",
            color = Color(0xFF3F51B5)
        )
    }

    Spacer(modifier = Modifier.height(12.dp))

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F7FF))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "أعلى فئة إنفاق", style = MaterialTheme.typography.titleSmall)
            Text(
                text = topCategory?.key ?: "لا توجد بيانات",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            if (topCategory != null) {
                Text(
                    text = "${currency.format(topCategory.value)} ر.س",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun SummaryCard(title: String, value: String, color: Color) {
    Card(
        modifier = Modifier.weight(1f),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.12f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = title, style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ExpenseItem(expense: Expense, onDelete: () -> Unit) {
    val currency = NumberFormat.getCurrencyInstance(Locale("ar", "SA"))
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                }
                Spacer(modifier = Modifier.size(12.dp))
                Column {
                    Text(text = expense.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(text = expense.category, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                    Text(text = expense.date, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    if (expense.note.isNotBlank()) {
                        Text(text = expense.note, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = currency.format(expense.amount),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.size(8.dp))
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "حذف",
                    modifier = Modifier.clickable { onDelete() },
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddExpenseDialog(
    onDismiss: () -> Unit,
    onSubmit: (String, String, String, String, String) -> Unit
) {
    val categories = listOf("طعام", "نقل", "فواتير", "تسوق", "تعليم", "أخرى")
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(categories.first()) }
    var date by remember { mutableStateOf(java.time.LocalDate.now().toString()) }
    var note by remember { mutableStateOf("") }

    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            androidx.compose.material3.TextButton(
                onClick = {
                    if (title.isNotBlank() && amount.isNotBlank()) {
                        onSubmit(title, amount, category, date, note)
                    }
                }
            ) {
                Text("إضافة")
            }
        },
        dismissButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) {
                Text("إلغاء")
            }
        },
        title = { Text("إضافة مصروف جديد") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("العنوان") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("المبلغ") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("التاريخ yyyy-MM-dd") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("ملاحظة") },
                    modifier = Modifier.fillMaxWidth()
                )
                androidx.compose.material3.ExposedDropdownMenuBox(
                    expanded = false,
                    onExpandedChange = {}
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        label = { Text("التصنيف") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true
                    )
                }
                androidx.compose.material3.DropdownMenu(
                    expanded = false,
                    onDismissRequest = {}
                ) {
                    categories.forEach { item ->
                        androidx.compose.material3.DropdownMenuItem(
                            text = { Text(item) },
                            onClick = { category = item }
                        )
                    }
                }
            }
        }
    )
}
