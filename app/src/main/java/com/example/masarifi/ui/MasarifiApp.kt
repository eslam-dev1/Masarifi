package com.example.masarifi.ui

import android.content.Context
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
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Restaurant
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
private val Canvas = Color(0xFFF7F8F4)
private val Muted = Color(0xFF7C8983)
private val SoftGreen = Color(0xFFE9F2EC)
private const val LANGUAGE_PREFS = "masarifi_settings"
private const val LANGUAGE_KEY = "language_english"

private fun text(key: String, english: Boolean): String {
    val en = mapOf(
        "greeting" to "Welcome 👋", "app" to "Masarifi", "month_expenses" to "Monthly expenses",
        "monthly_total" to "Total recorded this month", "quick_view" to "Quick overview",
        "last_7_days" to "Spending over the last 7 days", "by_category" to "By category",
        "this_month" to "This month", "first_category" to "Add your first expense to see category insights",
        "recent" to "Recent transactions", "transactions" to "transactions", "empty_title" to "No expenses yet",
        "empty_hint" to "Tap + to record your first expense", "new_expense" to "New expense",
        "expense_name" to "Expense name", "amount" to "Amount (EGP)", "category" to "Category",
        "date" to "Date (yyyy-MM-dd)", "optional_note" to "Optional note", "save" to "Save",
        "cancel" to "Cancel", "add" to "Add", "delete" to "Delete expense", "currency" to "EGP",
        "name_error" to "Enter an expense name", "amount_error" to "Enter an amount greater than zero",
        "date_error" to "Enter a date in yyyy-MM-dd format", "food" to "Food", "transport" to "Transport",
        "bills" to "Bills", "shopping" to "Shopping", "education" to "Education", "other" to "Other"
    )
    val ar = mapOf(
        "greeting" to "أهلاً بيك 👋", "app" to "مصاريفي", "month_expenses" to "مصروفات الشهر",
        "monthly_total" to "إجمالي ما سجلته خلال الشهر", "quick_view" to "نظرة سريعة",
        "last_7_days" to "مصروفات آخر ٧ أيام", "by_category" to "حسب التصنيف",
        "this_month" to "هذا الشهر", "first_category" to "سجّل مصروفك الأول لتظهر لك إحصائيات التصنيفات",
        "recent" to "آخر العمليات", "transactions" to "عملية", "empty_title" to "لسه مفيش مصاريف",
        "empty_hint" to "اضغط على + وسجّل أول مصروف", "new_expense" to "مصروف جديد",
        "expense_name" to "اسم المصروف", "amount" to "المبلغ بالجنيه", "category" to "التصنيف",
        "date" to "التاريخ (yyyy-MM-dd)", "optional_note" to "ملاحظة اختيارية", "save" to "حفظ",
        "cancel" to "إلغاء", "add" to "إضافة", "delete" to "حذف المصروف", "currency" to "ج.م",
        "name_error" to "اكتب اسم المصروف", "amount_error" to "اكتب مبلغًا صحيحًا أكبر من صفر",
        "date_error" to "اكتب التاريخ بصيغة yyyy-MM-dd", "food" to "طعام", "transport" to "نقل",
        "bills" to "فواتير", "shopping" to "تسوق", "education" to "تعليم", "other" to "أخرى"
    )
    return (if (english) en else ar)[key] ?: key
}

private fun categoryLabel(category: String, english: Boolean): String = when (category) {
    "طعام" -> text("food", english)
    "نقل" -> text("transport", english)
    "فواتير" -> text("bills", english)
    "تسوق" -> text("shopping", english)
    "تعليم" -> text("education", english)
    else -> text("other", english)
}

@Composable
fun MasarifiApp(viewModel: ExpenseViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val preferences = remember { context.getSharedPreferences(LANGUAGE_PREFS, Context.MODE_PRIVATE) }
    var english by remember { mutableStateOf(preferences.getBoolean(LANGUAGE_KEY, false)) }
    var showDialog by remember { mutableStateOf(false) }
    val locale = if (english) Locale.ENGLISH else Locale("ar", "EG")
    val currency = remember(english) { NumberFormat.getNumberInstance(locale).apply { maximumFractionDigits = 2 } }

    CompositionLocalProvider(LocalLayoutDirection provides if (english) LayoutDirection.Ltr else LayoutDirection.Rtl) {
        Scaffold(
            containerColor = Canvas,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showDialog = true },
                    shape = RoundedCornerShape(18.dp),
                    containerColor = Forest,
                    contentColor = Color.White
                ) { Icon(Icons.Default.Add, contentDescription = text("add", english)) }
            }
        ) { padding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(padding)
                    .verticalScroll(rememberScrollState()).padding(horizontal = 20.dp)
            ) {
                Spacer(Modifier.height(18.dp))
                Header(english) {
                    english = !english
                    preferences.edit().putBoolean(LANGUAGE_KEY, english).apply()
                }
                Spacer(Modifier.height(22.dp))
                MonthOverview(currency.format(state.totalThisMonth), english, locale)
                Spacer(Modifier.height(20.dp))
                WeeklySpending(state.expenses, currency, english, locale)
                Spacer(Modifier.height(22.dp))
                CategoriesSection(state.categoryTotals, currency, english)
                Spacer(Modifier.height(22.dp))
                RecentExpenses(state.expenses, currency, english, viewModel::deleteExpense)
                Spacer(Modifier.height(90.dp))
            }
        }

        if (showDialog) {
            AddExpenseDialog(english, onDismiss = { showDialog = false }) { title, amount, category, date, note ->
                viewModel.addExpense(title, amount, category, date, note)
                showDialog = false
            }
        }
    }
}

@Composable
private fun Header(english: Boolean, onLanguageClick: () -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Column {
            Text(text("greeting", english), color = Muted, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(3.dp))
            Text(text("app", english), color = Forest, fontSize = 27.sp, fontWeight = FontWeight.ExtraBold)
        }
        Surface(shape = RoundedCornerShape(50), color = Color.White, shadowElevation = 2.dp) {
            TextButton(onClick = onLanguageClick) {
                Icon(Icons.Default.Language, contentDescription = null, tint = Forest, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text(if (english) "العربية" else "English", color = Forest, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun MonthOverview(formattedTotal: String, english: Boolean, locale: Locale) {
    val monthName = remember(english) { LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM yyyy", locale)) }
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(28.dp), colors = CardDefaults.cardColors(containerColor = Forest), elevation = CardDefaults.cardElevation(5.dp)) {
        Column(Modifier.padding(22.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = Mint, modifier = Modifier.size(19.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(text("month_expenses", english), color = Color.White.copy(alpha = .82f), style = MaterialTheme.typography.titleSmall)
                }
                Surface(color = Color.White.copy(alpha = .12f), shape = RoundedCornerShape(50)) {
                    Text(monthName, Modifier.padding(horizontal = 12.dp, vertical = 7.dp), color = Color.White, style = MaterialTheme.typography.labelMedium)
                }
            }
            Spacer(Modifier.height(20.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(formattedTotal, color = Color.White, fontSize = 35.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.width(8.dp))
                Text(text("currency", english), color = Mint, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(bottom = 5.dp))
            }
            Spacer(Modifier.height(18.dp))
            Divider(color = Color.White.copy(alpha = .16f))
            Spacer(Modifier.height(14.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = CircleShape, color = Color.White.copy(alpha = .13f), modifier = Modifier.size(30.dp)) {
                    Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.ArrowOutward, contentDescription = null, tint = Mint, modifier = Modifier.size(16.dp)) }
                }
                Spacer(Modifier.width(9.dp))
                Text(text("monthly_total", english), color = Color.White.copy(alpha = .78f), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun WeeklySpending(expenses: List<Expense>, formatter: NumberFormat, english: Boolean, locale: Locale) {
    val today = LocalDate.now()
    val totals = (6 downTo 0).map { offset ->
        val day = today.minusDays(offset.toLong())
        day to expenses.filter { it.date == day.toString() }.sumOf { it.amount }
    }
    val maxAmount = totals.maxOfOrNull { it.second }?.coerceAtLeast(1.0) ?: 1.0
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(1.dp)) {
        Column(Modifier.padding(18.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(text("quick_view", english), color = Forest, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(3.dp))
                    Text(text("last_7_days", english), color = Muted, style = MaterialTheme.typography.bodySmall)
                }
                Surface(shape = RoundedCornerShape(12.dp), color = SoftGreen) {
                    Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = Forest, modifier = Modifier.padding(10.dp).size(18.dp))
                }
            }
            Spacer(Modifier.height(18.dp))
            Row(Modifier.fillMaxWidth().height(112.dp), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.Bottom) {
                totals.forEach { (day, amount) ->
                    val isToday = day == today
                    Column(Modifier.weight(1f).fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom) {
                        if (amount > 0) {
                            Text(formatter.format(amount), color = Muted, fontSize = 9.sp, maxLines = 1)
                            Spacer(Modifier.height(4.dp))
                        } else Spacer(Modifier.height(14.dp))
                        Box(Modifier.fillMaxWidth(.58f).height((15 + (75 * (amount / maxAmount)).toInt()).dp).clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)).background(if (isToday) Forest else Mint))
                        Spacer(Modifier.height(7.dp))
                        Text(day.format(DateTimeFormatter.ofPattern("E", locale)), color = if (isToday) Forest else Muted, fontSize = 10.sp, fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal)
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoriesSection(totals: Map<String, Double>, formatter: NumberFormat, english: Boolean) {
    val colors = listOf(Color(0xFF497A63), Color(0xFFE6A15A), Color(0xFF7793B2), Color(0xFFB18AAB), Color(0xFF8A9B70))
    val entries = totals.entries.sortedByDescending { it.value }.take(4)
    val max = entries.maxOfOrNull { it.value }?.coerceAtLeast(1.0) ?: 1.0
    Column {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text("by_category", english), color = Forest, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text("this_month", english), color = Muted, style = MaterialTheme.typography.bodySmall)
        }
        Spacer(Modifier.height(12.dp))
        if (entries.isEmpty()) {
            Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Text(text("first_category", english), Modifier.padding(16.dp), color = Muted, style = MaterialTheme.typography.bodyMedium)
            }
        } else {
            Card(shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(15.dp)) {
                    entries.forEachIndexed { index, entry ->
                        Column {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(Modifier.size(9.dp).clip(CircleShape).background(colors[index % colors.size]))
                                    Spacer(Modifier.width(8.dp))
                                    Text(categoryLabel(entry.key, english), color = Forest, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                                }
                                Text("${formatter.format(entry.value)} ${text("currency", english)}", color = Forest, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                            }
                            Spacer(Modifier.height(8.dp))
                            Box(Modifier.fillMaxWidth().height(6.dp).clip(CircleShape).background(Canvas)) {
                                Box(Modifier.fillMaxWidth((entry.value / max).toFloat().coerceIn(.04f, 1f)).height(6.dp).clip(CircleShape).background(colors[index % colors.size]))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RecentExpenses(expenses: List<Expense>, formatter: NumberFormat, english: Boolean, onDelete: (Long) -> Unit) {
    Column {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text("recent", english), color = Forest, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Surface(color = SoftGreen, shape = RoundedCornerShape(50)) {
                Text("${expenses.size} ${text("transactions", english)}", color = Forest, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 11.dp, vertical = 6.dp))
            }
        }
        Spacer(Modifier.height(10.dp))
        if (expenses.isEmpty()) {
            Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Wallet, contentDescription = null, tint = Mint, modifier = Modifier.size(34.dp))
                    Spacer(Modifier.height(8.dp))
                    Text(text("empty_title", english), color = Forest, fontWeight = FontWeight.SemiBold)
                    Text(text("empty_hint", english), color = Muted, style = MaterialTheme.typography.bodySmall)
                }
            }
        } else {
            Card(shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column {
                    expenses.take(8).forEachIndexed { index, expense ->
                        ExpenseRow(expense, formatter, english) { onDelete(expense.id) }
                        if (index < minOf(expenses.size, 8) - 1) Divider(Modifier.padding(start = 68.dp, end = 16.dp), color = Canvas)
                    }
                }
            }
        }
    }
}

@Composable
private fun ExpenseRow(expense: Expense, formatter: NumberFormat, english: Boolean, onDelete: () -> Unit) {
    val categoryColor = when (expense.category) {
        "طعام" -> Color(0xFFE8A15B)
        "نقل" -> Color(0xFF6D8EAA)
        "تسوق" -> Color(0xFFB18AAB)
        "فواتير" -> Color(0xFF7F9C76)
        else -> Forest
    }
    val categoryIcon = when (expense.category) {
        "طعام" -> Icons.Default.Restaurant
        "نقل" -> Icons.Default.DirectionsCar
        "تسوق" -> Icons.Default.ShoppingBag
        else -> Icons.Default.MoreHoriz
    }
    Row(Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 13.dp), verticalAlignment = Alignment.CenterVertically) {
        Surface(shape = RoundedCornerShape(14.dp), color = categoryColor.copy(alpha = .13f), modifier = Modifier.size(42.dp)) {
            Box(contentAlignment = Alignment.Center) { Icon(categoryIcon, contentDescription = null, tint = categoryColor, modifier = Modifier.size(20.dp)) }
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(expense.title, color = Forest, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(Modifier.height(3.dp))
            Text("${categoryLabel(expense.category, english)} • ${expense.date}", color = Muted, style = MaterialTheme.typography.labelSmall, maxLines = 1)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text("-${formatter.format(expense.amount)}", color = Forest, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            Text(text("currency", english), color = Muted, style = MaterialTheme.typography.labelSmall)
        }
        IconButton(onClick = onDelete, modifier = Modifier.size(34.dp)) {
            Icon(Icons.Default.DeleteOutline, contentDescription = text("delete", english), tint = Muted, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun AddExpenseDialog(english: Boolean, onDismiss: () -> Unit, onSubmit: (String, String, String, String, String) -> Unit) {
    val categories = listOf("طعام", "نقل", "فواتير", "تسوق", "تعليم", "أخرى")
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(categories.first()) }
    var date by remember { mutableStateOf(LocalDate.now().toString()) }
    var note by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text("new_expense", english), color = Forest, fontWeight = FontWeight.Bold) },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(title, { title = it }, label = { Text(text("expense_name", english)) }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                OutlinedTextField(amount, { amount = it }, label = { Text(text("amount", english)) }, modifier = Modifier.fillMaxWidth(), singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
                Box {
                    OutlinedTextField(categoryLabel(category, english), {}, label = { Text(text("category", english)) }, modifier = Modifier.fillMaxWidth().clickable { expanded = true }, readOnly = true, trailingIcon = { Icon(Icons.Default.MoreHoriz, contentDescription = null) })
                    Box(Modifier.matchParentSize().clickable { expanded = true })
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        categories.forEach { item -> DropdownMenuItem(text = { Text(categoryLabel(item, english)) }, onClick = { category = item; expanded = false }) }
                    }
                }
                OutlinedTextField(date, { date = it }, label = { Text(text("date", english)) }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                OutlinedTextField(note, { note = it }, label = { Text(text("optional_note", english)) }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                if (error.isNotBlank()) Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val validDate = runCatching { LocalDate.parse(date); true }.getOrDefault(false)
                error = when {
                    title.isBlank() -> text("name_error", english)
                    (amount.toDoubleOrNull() ?: 0.0) <= 0.0 -> text("amount_error", english)
                    !validDate -> text("date_error", english)
                    else -> ""
                }
                if (error.isBlank()) onSubmit(title, amount, category, date, note)
            }) { Text(text("save", english), color = Forest, fontWeight = FontWeight.Bold) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(text("cancel", english), color = Muted) } },
        containerColor = Color.White,
        shape = RoundedCornerShape(26.dp)
    )
}
