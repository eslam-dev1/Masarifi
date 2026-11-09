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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.masarifi.data.Expense
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val Forest = Color(0xFF173B32)
private val Mint = Color(0xFFB7E4C7)
private val Muted = Color(0xFF7C8983)
private const val LANGUAGE_PREFS = "masarifi_settings"
private const val LANGUAGE_KEY = "language_english"

private fun tr(key: String, en: Boolean) = (if (en) mapOf(
    "greeting" to "Welcome", "month" to "Monthly spending", "budget" to "Monthly budget", "set_budget" to "Set budget",
    "spent" to "Spent", "remaining" to "Remaining", "over_budget" to "Over budget", "today" to "Today",
    "quick" to "Last 7 days", "categories" to "Spending by category", "recent" to "Recent expenses",
    "empty" to "No expenses yet", "empty_hint" to "Tap + to add your first expense", "count" to "transactions",
    "new" to "New expense", "edit" to "Edit expense", "name" to "Expense name", "amount" to "Amount (EGP)",
    "category" to "Category", "date" to "Date (yyyy-MM-dd)", "note" to "Note (optional)", "save" to "Save",
    "cancel" to "Cancel", "food" to "Food", "transport" to "Transport", "bills" to "Bills",
    "shopping" to "Shopping", "education" to "Education", "other" to "Other", "currency" to "EGP",
    "budget_title" to "Monthly budget", "budget_hint" to "Enter your spending limit for this month",
    "budget_invalid" to "Enter a valid amount", "expense_invalid" to "Enter a name, positive amount and valid date",
    "delete" to "Delete expense", "language" to "العربية", "dark" to "Switch to light mode", "light" to "Switch to dark mode"
) else mapOf(
    "greeting" to "أهلاً بيك", "month" to "مصروفات الشهر", "budget" to "ميزانية الشهر", "set_budget" to "تحديد الميزانية",
    "spent" to "المصروف", "remaining" to "المتبقي", "over_budget" to "تجاوزت الميزانية", "today" to "اليوم",
    "quick" to "مصروفات آخر ٧ أيام", "categories" to "المصروفات حسب التصنيف", "recent" to "آخر المصروفات",
    "empty" to "لسه مفيش مصاريف", "empty_hint" to "اضغط + لإضافة أول مصروف", "count" to "عملية",
    "new" to "مصروف جديد", "edit" to "تعديل المصروف", "name" to "اسم المصروف", "amount" to "المبلغ بالجنيه",
    "category" to "التصنيف", "date" to "التاريخ (yyyy-MM-dd)", "note" to "ملاحظة اختيارية", "save" to "حفظ",
    "cancel" to "إلغاء", "food" to "طعام", "transport" to "نقل", "bills" to "فواتير",
    "shopping" to "تسوق", "education" to "تعليم", "other" to "أخرى", "currency" to "ج.م",
    "budget_title" to "ميزانية الشهر", "budget_hint" to "اكتب الحد الأقصى لمصروفات الشهر",
    "budget_invalid" to "اكتب مبلغًا صحيحًا", "expense_invalid" to "تأكد من الاسم والمبلغ والتاريخ",
    "delete" to "حذف المصروف", "language" to "English", "dark" to "التبديل للوضع الفاتح", "light" to "التبديل للوضع الداكن"
))[key] ?: key

private fun categoryName(value: String, en: Boolean) = when (value) {
    "طعام" -> tr("food", en); "نقل" -> tr("transport", en); "فواتير" -> tr("bills", en)
    "تسوق" -> tr("shopping", en); "تعليم" -> tr("education", en); else -> tr("other", en)
}

@Composable
fun MasarifiApp(
    darkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    viewModel: ExpenseViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences(LANGUAGE_PREFS, android.content.Context.MODE_PRIVATE) }
    var english by remember { mutableStateOf(prefs.getBoolean(LANGUAGE_KEY, false)) }
    var showExpenseDialog by remember { mutableStateOf(false) }
    var showBudgetDialog by remember { mutableStateOf(false) }
    var editing by remember { mutableStateOf<Expense?>(null) }
    val locale = if (english) Locale.ENGLISH else Locale("ar", "EG")
    val number = remember(english) { NumberFormat.getNumberInstance(locale).apply { maximumFractionDigits = 2 } }

    CompositionLocalProvider(LocalLayoutDirection provides if (english) LayoutDirection.Ltr else LayoutDirection.Rtl) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            floatingActionButton = {
                FloatingActionButton(onClick = { editing = null; showExpenseDialog = true }, containerColor = Forest, contentColor = Color.White, shape = RoundedCornerShape(18.dp)) {
                    Icon(Icons.Default.Add, contentDescription = tr("new", english))
                }
            }
        ) { padding ->
            Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp)) {
                Spacer(Modifier.height(18.dp))
                Header(english, darkMode, onLanguage = {
                    english = !english
                    prefs.edit().putBoolean(LANGUAGE_KEY, english).apply()
                }, onTheme = onToggleDarkMode)
                Spacer(Modifier.height(20.dp))
                MonthCard(state.totalThisMonth, number, english, locale)
                Spacer(Modifier.height(14.dp))
                BudgetCard(state.monthlyBudget, state.totalThisMonth, number, english) { showBudgetDialog = true }
                Spacer(Modifier.height(20.dp))
                WeekCard(state.expenses, number, english, locale)
                Spacer(Modifier.height(22.dp))
                CategoryCard(state.categoryTotals, number, english)
                Spacer(Modifier.height(22.dp))
                RecentCard(state.expenses, number, english, onDelete = viewModel::deleteExpense, onEdit = {
                    editing = it
                    showExpenseDialog = true
                })
                Spacer(Modifier.height(90.dp))
            }
        }

        if (showBudgetDialog) {
            AmountDialog(
                title = tr("budget_title", english), hint = tr("budget_hint", english), initialValue = if (state.monthlyBudget > 0) state.monthlyBudget.toString() else "",
                errorText = tr("budget_invalid", english), english = english,
                onDismiss = { showBudgetDialog = false }, onSave = { value -> viewModel.saveMonthlyBudget(value); showBudgetDialog = false }
            )
        }
        if (showExpenseDialog) {
            ExpenseDialog(english, editing, onDismiss = { showExpenseDialog = false; editing = null }) { title, amount, category, date, note, id ->
                viewModel.saveExpense(title, amount, category, date, note, id)
                showExpenseDialog = false
                editing = null
            }
        }
    }
}

@Composable
private fun Header(english: Boolean, darkMode: Boolean, onLanguage: () -> Unit, onTheme: () -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Column {
            Text("${tr("greeting", english)} 👋", color = Muted, style = MaterialTheme.typography.bodyMedium)
            Text(if (english) "Masarifi" else "مصاريفي", color = MaterialTheme.colorScheme.onBackground, fontSize = 27.sp, fontWeight = FontWeight.ExtraBold)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onTheme) { Icon(if (darkMode) Icons.Default.LightMode else Icons.Default.DarkMode, contentDescription = tr(if (darkMode) "light" else "dark", english), tint = MaterialTheme.colorScheme.primary) }
            Surface(shape = RoundedCornerShape(50), color = MaterialTheme.colorScheme.surface, shadowElevation = 1.dp) {
                TextButton(onClick = onLanguage) {
                    Icon(Icons.Default.Language, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(17.dp))
                    Spacer(Modifier.width(5.dp))
                    Text(tr("language", english), color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Composable
private fun MonthCard(total: Double, number: NumberFormat, english: Boolean, locale: Locale) {
    val month = remember(english) { LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM yyyy", locale)) }
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(26.dp), colors = CardDefaults.cardColors(containerColor = Forest), elevation = CardDefaults.cardElevation(4.dp)) {
        Column(Modifier.padding(21.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarMonth, null, tint = Mint, modifier = Modifier.size(19.dp))
                    Spacer(Modifier.width(8.dp)); Text(tr("month", english), color = Color.White.copy(alpha = .85f), style = MaterialTheme.typography.titleSmall)
                }
                Surface(color = Color.White.copy(alpha = .12f), shape = CircleShape) { Text(month, Modifier.padding(horizontal = 11.dp, vertical = 6.dp), color = Color.White, style = MaterialTheme.typography.labelSmall) }
            }
            Spacer(Modifier.height(20.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(number.format(total), color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.width(8.dp)); Text(tr("currency", english), color = Mint, modifier = Modifier.padding(bottom = 5.dp))
            }
            Spacer(Modifier.height(8.dp))
            Text("${tr("today", english)}: ${number.format(0)} ${tr("currency", english)}", color = Color.White.copy(alpha = .7f), style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun BudgetCard(budget: Double, spent: Double, number: NumberFormat, english: Boolean, onClick: () -> Unit) {
    val remaining = budget - spent
    val ratio = if (budget > 0) (spent / budget).toFloat().coerceIn(0f, 1f) else 0f
    Card(Modifier.fillMaxWidth().clickable(onClick = onClick), shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(17.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Wallet, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp)); Text(tr("budget", english), color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                }
                Text(if (budget > 0) tr("set_budget", english) else "+ ${tr("set_budget", english)}", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelMedium)
            }
            Spacer(Modifier.height(12.dp))
            if (budget <= 0) {
                Text(tr("budget_hint", english), color = Muted, style = MaterialTheme.typography.bodySmall)
            } else {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("${tr("spent", english)}: ${number.format(spent)} ${tr("currency", english)}", color = Muted, style = MaterialTheme.typography.bodySmall)
                    Text("${tr(if (remaining >= 0) "remaining" else "over_budget", english)}: ${number.format(kotlin.math.abs(remaining))} ${tr("currency", english)}", color = if (remaining >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                }
                Spacer(Modifier.height(9.dp))
                LinearProgressIndicator(progress = { ratio }, modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape), color = if (spent > budget) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary, trackColor = MaterialTheme.colorScheme.surfaceVariant)
                Spacer(Modifier.height(6.dp))
                Text("${tr("budget", english)}: ${number.format(budget)} ${tr("currency", english)}", color = Muted, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
private fun WeekCard(expenses: List<Expense>, number: NumberFormat, english: Boolean, locale: Locale) {
    val today = LocalDate.now()
    val days = (6 downTo 0).map { today.minusDays(it.toLong()) }
    val values = days.map { day -> expenses.filter { it.date == day.toString() }.sumOf { it.amount } }
    val max = (values.maxOrNull() ?: 0.0).coerceAtLeast(1.0)
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(17.dp)) {
            Text(tr("quick", english), color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth().height(105.dp), horizontalArrangement = Arrangement.spacedBy(9.dp), verticalAlignment = Alignment.Bottom) {
                days.forEachIndexed { index, day ->
                    Column(Modifier.weight(1f).fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom) {
                        val amount = values[index]
                        Box(Modifier.fillMaxWidth(.56f).height((12 + 65 * (amount / max).toInt().coerceAtLeast(if (amount > 0) 1 else 0)).dp).clip(RoundedCornerShape(topStart = 7.dp, topEnd = 7.dp)).background(if (day == today) MaterialTheme.colorScheme.primary else Mint))
                        Spacer(Modifier.height(6.dp))
                        Text(day.format(DateTimeFormatter.ofPattern("E", locale)), color = Muted, fontSize = 9.sp, maxLines = 1)
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryCard(totals: Map<String, Double>, number: NumberFormat, english: Boolean) {
    val entries = totals.entries.sortedByDescending { it.value }.take(5)
    val max = (entries.maxOfOrNull { it.value } ?: 0.0).coerceAtLeast(1.0)
    Column {
        Text(tr("categories", english), color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(10.dp))
        Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            if (entries.isEmpty()) Text(tr("empty_hint", english), Modifier.padding(16.dp), color = Muted)
            else Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                entries.forEach { entry ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(categoryName(entry.key, english), color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodyMedium)
                                Text("${number.format(entry.value)} ${tr("currency", english)}", color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodySmall)
                            }
                            Spacer(Modifier.height(7.dp))
                            LinearProgressIndicator(progress = { (entry.value / max).toFloat().coerceIn(.03f, 1f) }, modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape), color = MaterialTheme.colorScheme.secondary, trackColor = MaterialTheme.colorScheme.surfaceVariant)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RecentCard(expenses: List<Expense>, number: NumberFormat, english: Boolean, onDelete: (Long) -> Unit, onEdit: (Expense) -> Unit) {
    Column {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(tr("recent", english), color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("${expenses.size} ${tr("count", english)}", color = Muted, style = MaterialTheme.typography.labelSmall)
        }
        Spacer(Modifier.height(9.dp))
        Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            if (expenses.isEmpty()) Column(Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.ReceiptLong, null, tint = Mint, modifier = Modifier.size(32.dp))
                Spacer(Modifier.height(8.dp)); Text(tr("empty", english), color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold)
                Text(tr("empty_hint", english), color = Muted, style = MaterialTheme.typography.bodySmall)
            } else Column {
                expenses.take(30).forEachIndexed { index, expense ->
                    ExpenseRow(expense, number, english, onEdit = { onEdit(expense) }, onDelete = { onDelete(expense.id) })
                    if (index < minOf(expenses.size, 30) - 1) Divider(Modifier.padding(start = 60.dp, end = 14.dp), color = MaterialTheme.colorScheme.surfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun ExpenseRow(expense: Expense, number: NumberFormat, english: Boolean, onEdit: () -> Unit, onDelete: () -> Unit) {
    val iconTint = when (expense.category) { "طعام" -> Color(0xFFE6A15A); "نقل" -> Color(0xFF7793B2); "تسوق" -> Color(0xFFB18AAB); else -> MaterialTheme.colorScheme.secondary }
    Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 11.dp), verticalAlignment = Alignment.CenterVertically) {
        Surface(shape = RoundedCornerShape(13.dp), color = iconTint.copy(alpha = .16f), modifier = Modifier.size(40.dp)) {
            Box(contentAlignment = Alignment.Center) { Icon(when (expense.category) { "طعام" -> Icons.Default.Restaurant; "نقل" -> Icons.Default.DirectionsCar; "تسوق" -> Icons.Default.ShoppingBag; else -> Icons.Default.MoreHoriz }, null, tint = iconTint, modifier = Modifier.size(19.dp)) }
        }
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text(expense.title, color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text("${categoryName(expense.category, english)} • ${expense.date}", color = Muted, style = MaterialTheme.typography.labelSmall, maxLines = 1)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(number.format(expense.amount), color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            Text(tr("currency", english), color = Muted, style = MaterialTheme.typography.labelSmall)
        }
        IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) { Icon(Icons.Default.Edit, contentDescription = tr("edit", english), tint = Muted, modifier = Modifier.size(17.dp)) }
        IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) { Icon(Icons.Default.DeleteOutline, contentDescription = tr("delete", english), tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(17.dp)) }
    }
}

@Composable
private fun AmountDialog(title: String, hint: String, initialValue: String, errorText: String, english: Boolean, onDismiss: () -> Unit, onSave: (String) -> Unit) {
    var value by remember { mutableStateOf(initialValue) }
    var error by remember { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, fontWeight = FontWeight.Bold) },
        text = { Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(hint, color = Muted)
            OutlinedTextField(value, { value = it }, label = { Text(tr("amount", english)) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), singleLine = true)
            if (error) Text(errorText, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        } },
        confirmButton = { TextButton(onClick = { val amount = value.toDoubleOrNull(); if (amount == null || amount <= 0) error = true else onSave(value) }) { Text(tr("save", english)) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text(tr("cancel", english)) } },
        containerColor = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(24.dp)
    )
}

@Composable
private fun ExpenseDialog(english: Boolean, expense: Expense?, onDismiss: () -> Unit, onSave: (String, String, String, String, String, Long?) -> Unit) {
    val categories = listOf("طعام", "نقل", "فواتير", "تسوق", "تعليم", "أخرى")
    var title by remember(expense?.id) { mutableStateOf(expense?.title ?: "") }
    var amount by remember(expense?.id) { mutableStateOf(expense?.amount?.toString() ?: "") }
    var category by remember(expense?.id) { mutableStateOf(expense?.category ?: categories.first()) }
    var date by remember(expense?.id) { mutableStateOf(expense?.date ?: LocalDate.now().toString()) }
    var note by remember(expense?.id) { mutableStateOf(expense?.note ?: "") }
    var expanded by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(tr(if (expense == null) "new" else "edit", english), fontWeight = FontWeight.Bold) },
        text = { Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(title, { title = it }, label = { Text(tr("name", english)) }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(amount, { amount = it }, label = { Text(tr("amount", english)) }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), singleLine = true)
            Box {
                OutlinedTextField(categoryName(category, english), {}, label = { Text(tr("category", english)) }, modifier = Modifier.fillMaxWidth().clickable { expanded = true }, readOnly = true, trailingIcon = { Icon(Icons.Default.MoreHoriz, null) })
                Box(Modifier.matchParentSize().clickable { expanded = true })
                DropdownMenu(expanded, { expanded = false }) { categories.forEach { item -> DropdownMenuItem(text = { Text(categoryName(item, english)) }, onClick = { category = item; expanded = false }) } }
            }
            OutlinedTextField(date, { date = it }, label = { Text(tr("date", english)) }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(note, { note = it }, label = { Text(tr("note", english)) }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            if (error) Text(tr("expense_invalid", english), color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        } },
        confirmButton = { TextButton(onClick = {
            val validDate = runCatching { LocalDate.parse(date); true }.getOrDefault(false)
            val valid = title.isNotBlank() && (amount.toDoubleOrNull() ?: 0.0) > 0 && validDate
            error = !valid
            if (valid) onSave(title, amount, category, date, note, expense?.id)
        }) { Text(tr("save", english)) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text(tr("cancel", english)) } },
        containerColor = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(24.dp)
    )
}
