package com.example.masarifi.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
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

@Composable
fun MasarifiApp(viewModel: ExpenseViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    val currency = remember { NumberFormat.getNumberInstance(Locale("ar", "EG")).apply { maximumFractionDigits = 2 } }

    Scaffold(
        containerColor = Canvas,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                shape = RoundedCornerShape(18.dp),
                containerColor = Forest,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "إضافة مصروف")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(18.dp))
            Header()
            Spacer(Modifier.height(22.dp))
            MonthOverview(total = state.totalThisMonth, formattedTotal = currency.format(state.totalThisMonth))
            Spacer(Modifier.height(20.dp))
            WeeklySpending(expenses = state.expenses, formatter = currency)
            Spacer(Modifier.height(22.dp))
            CategoriesSection(totals = state.categoryTotals, formatter = currency)
            Spacer(Modifier.height(22.dp))
            RecentExpenses(
                expenses = state.expenses,
                formatter = currency,
                onDelete = viewModel::deleteExpense
            )
            Spacer(Modifier.height(90.dp))
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
private fun Header() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("أهلاً بيك 👋", color = Muted, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(3.dp))
            Text("مصاريفي", color = Forest, fontSize = 27.sp, fontWeight = FontWeight.ExtraBold)
        }
        Surface(shape = CircleShape, color = Color.White, shadowElevation = 2.dp) {
            IconButton(onClick = { }) {
                Icon(Icons.Default.FavoriteBorder, contentDescription = "المفضلة", tint = Forest)
            }
        }
    }
}

@Composable
private fun MonthOverview(total: Double, formattedTotal: String) {
    val monthName = remember {
        LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale("ar")))
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Forest),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(modifier = Modifier.padding(22.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = Mint, modifier = Modifier.size(19.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("مصروفات الشهر", color = Color.White.copy(alpha = 0.82f), style = MaterialTheme.typography.titleSmall)
                }
                Surface(color = Color.White.copy(alpha = 0.12f), shape = RoundedCornerShape(50)) {
                    Text(monthName, modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp), color = Color.White, style = MaterialTheme.typography.labelMedium)
                }
            }
            Spacer(Modifier.height(20.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(formattedTotal, color = Color.White, fontSize = 35.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.width(8.dp))
                Text("ج.م", color = Mint, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(bottom = 5.dp))
            }
            Spacer(Modifier.height(18.dp))
            Divider(color = Color.White.copy(alpha = 0.16f))
            Spacer(Modifier.height(14.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = CircleShape, color = Color.White.copy(alpha = 0.13f), modifier = Modifier.size(30.dp)) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.ArrowOutward, contentDescription = null, tint = Mint, modifier = Modifier.size(16.dp))
                    }
                }
                Spacer(Modifier.width(9.dp))
                Text("إجمالي ما سجلته خلال الشهر", color = Color.White.copy(alpha = 0.78f), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun WeeklySpending(expenses: List<Expense>, formatter: NumberFormat) {
    val today = LocalDate.now()
    val days = (6 downTo 0).map { today.minusDays(it.toLong()) }
    val totals = days.map { day ->
        day to expenses.filter { it.date == day.toString() }.sumOf { it.amount }
    }
    val maxAmount = totals.maxOfOrNull { it.second }?.coerceAtLeast(1.0) ?: 1.0

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.padding(18.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("نظرة سريعة", color = Forest, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(3.dp))
                    Text("مصروفات آخر ٧ أيام", color = Muted, style = MaterialTheme.typography.bodySmall)
                }
                Surface(shape = RoundedCornerShape(12.dp), color = SoftGreen) {
                    Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = Forest, modifier = Modifier.padding(10.dp).size(18.dp))
                }
            }
            Spacer(Modifier.height(18.dp))
            Row(
                modifier = Modifier.fillMaxWidth().height(112.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                totals.forEach { (day, amount) ->
                    val isToday = day == today
                    Column(Modifier.weight(1f).fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom) {
                        if (amount > 0) {
                            Text(formatter.format(amount), color = Muted, fontSize = 9.sp, maxLines = 1)
                            Spacer(Modifier.height(4.dp))
                        } else {
                            Spacer(Modifier.height(14.dp))
                        }
                        Box(
                            Modifier
                                .fillMaxWidth(0.58f)
                                .height((15 + (75 * (amount / maxAmount)).toInt()).dp)
                                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                .background(if (isToday) Forest else Mint)
                        )
                        Spacer(Modifier.height(7.dp))
                        Text(day.format(DateTimeFormatter.ofPattern("E", Locale("ar"))), color = if (isToday) Forest else Muted, fontSize = 10.sp, fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal)
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoriesSection(totals: Map<String, Double>, formatter: NumberFormat) {
    val colors = listOf(Color(0xFF497A63), Color(0xFFE6A15A), Color(0xFF7793B2), Color(0xFFB18AAB), Color(0xFF8A9B70))
    val entries = totals.entries.sortedByDescending { it.value }.take(4)
    val max = entries.maxOfOrNull { it.value }?.coerceAtLeast(1.0) ?: 1.0
    Column {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("حسب التصنيف", color = Forest, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("هذا الشهر", color = Muted, style = MaterialTheme.typography.bodySmall)
        }
        Spacer(Modifier.height(12.dp))
        if (entries.isEmpty()) {
            Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Text("سجّل مصروفك الأول لتظهر لك إحصائيات التصنيفات", modifier = Modifier.padding(16.dp), color = Muted, style = MaterialTheme.typography.bodyMedium)
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
                                    Text(entry.key, color = Forest, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                                }
                                Text("${formatter.format(entry.value)} ج.م", color = Forest, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                            }
                            Spacer(Modifier.height(8.dp))
                            Box(Modifier.fillMaxWidth().height(6.dp).clip(CircleShape).background(Canvas)) {
                                Box(Modifier.fillMaxWidth((entry.value / max).toFloat().coerceIn(0.04f, 1f)).height(6.dp).clip(CircleShape).background(colors[index % colors.size]))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RecentExpenses(expenses: List<Expense>, formatter: NumberFormat, onDelete: (Long) -> Unit) {
    Column {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("آخر العمليات", color = Forest, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Surface(color = SoftGreen, shape = RoundedCornerShape(50)) {
                Text("${expenses.size} عملية", color = Forest, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 11.dp, vertical = 6.dp))
            }
        }
        Spacer(Modifier.height(10.dp))
        if (expenses.isEmpty()) {
            Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Wallet, contentDescription = null, tint = Mint, modifier = Modifier.size(34.dp))
                    Spacer(Modifier.height(8.dp))
                    Text("لسه مفيش مصاريف", color = Forest, fontWeight = FontWeight.SemiBold)
                    Text("اضغط على + وسجّل أول مصروف", color = Muted, style = MaterialTheme.typography.bodySmall)
                }
            }
        } else {
            Card(shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column {
                    expenses.take(8).forEachIndexed { index, expense ->
                        ExpenseRow(expense, formatter) { onDelete(expense.id) }
                        if (index < minOf(expenses.size, 8) - 1) {
                            Divider(modifier = Modifier.padding(start = 68.dp, end = 16.dp), color = Canvas)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ExpenseRow(expense: Expense, formatter: NumberFormat, onDelete: () -> Unit) {
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
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(shape = RoundedCornerShape(14.dp), color = categoryColor.copy(alpha = 0.13f), modifier = Modifier.size(42.dp)) {
            Box(contentAlignment = Alignment.Center) {
                Icon(categoryIcon, contentDescription = null, tint = categoryColor, modifier = Modifier.size(20.dp))
            }
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(expense.title, color = Forest, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(Modifier.height(3.dp))
            Text("${expense.category} • ${expense.date}", color = Muted, style = MaterialTheme.typography.labelSmall, maxLines = 1)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text("-${formatter.format(expense.amount)}", color = Forest, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            Text("ج.م", color = Muted, style = MaterialTheme.typography.labelSmall)
        }
        IconButton(onClick = onDelete, modifier = Modifier.size(34.dp)) {
            Icon(Icons.Default.DeleteOutline, contentDescription = "حذف المصروف", tint = Muted, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun AddExpenseDialog(onDismiss: () -> Unit, onSubmit: (String, String, String, String, String) -> Unit) {
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
        title = { Text("مصروف جديد", color = Forest, fontWeight = FontWeight.Bold) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(title, { title = it }, label = { Text("اسم المصروف") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                OutlinedTextField(amount, { amount = it }, label = { Text("المبلغ بالجنيه") }, modifier = Modifier.fillMaxWidth(), singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
                Box {
                    OutlinedTextField(category, {}, label = { Text("التصنيف") }, modifier = Modifier.fillMaxWidth().clickable { expanded = true }, readOnly = true, trailingIcon = { Icon(Icons.Default.MoreHoriz, contentDescription = null) })
                    Box(Modifier.matchParentSize().clickable { expanded = true })
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        categories.forEach { item ->
                            DropdownMenuItem(text = { Text(item) }, onClick = { category = item; expanded = false })
                        }
                    }
                }
                OutlinedTextField(date, { date = it }, label = { Text("التاريخ (yyyy-MM-dd)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                OutlinedTextField(note, { note = it }, label = { Text("ملاحظة اختيارية") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                if (error.isNotBlank()) Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val validDate = runCatching { LocalDate.parse(date); true }.getOrDefault(false)
                when {
                    title.isBlank() -> error = "اكتب اسم المصروف"
                    (amount.toDoubleOrNull() ?: 0.0) <= 0.0 -> error = "اكتب مبلغًا صحيحًا أكبر من صفر"
                    !validDate -> error = "اكتب التاريخ بصيغة yyyy-MM-dd"
                    else -> onSubmit(title, amount, category, date, note)
                }
            }) { Text("حفظ", color = Forest, fontWeight = FontWeight.Bold) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("إلغاء", color = Muted) } },
        containerColor = Color.White,
        shape = RoundedCornerShape(26.dp)
    )
}
