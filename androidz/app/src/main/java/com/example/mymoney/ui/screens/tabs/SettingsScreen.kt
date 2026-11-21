package com.example.mymoney.ui.screens.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mymoney.data.model.UserProfile
import com.example.mymoney.data.model.WalletAccount
import com.example.mymoney.ui.components.CategoryUiHelper
import com.example.mymoney.ui.theme.*
import com.example.mymoney.ui.viewmodel.FinanceViewModel

@Composable
fun SettingsScreen(
    userProfile: UserProfile?,
    financeViewModel: FinanceViewModel,
    onOpenAuth: () -> Unit,
    onOpenReports: () -> Unit,
    onOpenPrintPreview: () -> Unit,
    onOpenNextMonthPlan: () -> Unit,
    onOpenLogout: () -> Unit
) {
    val isLoggedIn = !userProfile?.email.isNullOrBlank()
    val totalBalance = financeViewModel.getTotalBalance()
    val displayName = userProfile?.displayName?.takeIf { it.isNotBlank() } ?: "ABC"
    val displayEmail = userProfile?.email?.takeIf { it.isNotBlank() } ?: "abc@gmail.com (Chế độ khách)"

    var editingWallet by remember { mutableStateOf<WalletAccount?>(null) }
    var showAddWalletDialog by remember { mutableStateOf(false) }
    var showCategoryDialog by remember { mutableStateOf(false) }

    if (editingWallet != null) {
        val w = editingWallet!!
        var balanceText by remember(w.id) { mutableStateOf((w.balance?.toLong() ?: 0L).toString()) }
        AlertDialog(
            onDismissRequest = { editingWallet = null },
            title = { Text("Cập nhật số dư: ${w.name}", fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = balanceText,
                    onValueChange = { v -> balanceText = v.filter { it.isDigit() } },
                    label = { Text("Số dư mới (VND)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val parsed = balanceText.toDoubleOrNull() ?: (w.balance ?: 0.0)
                        financeViewModel.updateWalletBalance(w.id, parsed)
                        editingWallet = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VcnOrange, contentColor = Color.White)
                ) {
                    Text("Lưu số dư")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingWallet = null }) { Text("Hủy") }
            }
        )
    }

    if (showAddWalletDialog) {
        var newWalletName by remember { mutableStateOf("") }
        var newWalletSub by remember { mutableStateOf("") }
        var newWalletBal by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showAddWalletDialog = false },
            title = { Text("Thêm ví / tài khoản mới", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newWalletName,
                        onValueChange = { newWalletName = it },
                        label = { Text("Tên ví (vd: MB Bank)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newWalletSub,
                        onValueChange = { newWalletSub = it },
                        label = { Text("Mô tả ngắn") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newWalletBal,
                        onValueChange = { v -> newWalletBal = v.filter { it.isDigit() } },
                        label = { Text("Số dư ban đầu (VND)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newWalletName.isNotBlank()) {
                            financeViewModel.addWallet(
                                name = newWalletName,
                                subtitle = newWalletSub,
                                balance = newWalletBal.toDoubleOrNull() ?: 0.0
                            )
                            showAddWalletDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VcnOrange, contentColor = Color.White)
                ) {
                    Text("Thêm ví")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddWalletDialog = false }) { Text("Hủy") }
            }
        )
    }

    if (showCategoryDialog) {
        var newCatName by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showCategoryDialog = false },
            title = { Text("Danh mục mặc định (${financeViewModel.categories.size})", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = financeViewModel.categories.joinToString(" • ") { it.name ?: "" },
                        style = MaterialTheme.typography.bodySmall,
                        color = VcnTextSecondary
                    )
                    HorizontalDivider(color = VcnDividerGray, modifier = Modifier.padding(vertical = 4.dp))
                    OutlinedTextField(
                        value = newCatName,
                        onValueChange = { newCatName = it },
                        label = { Text("Thêm danh mục chi tiêu mới") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newCatName.isNotBlank()) {
                            financeViewModel.addCategory(newCatName, isExpense = true)
                            newCatName = ""
                        } else {
                            showCategoryDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VcnOrange, contentColor = Color.White)
                ) {
                    Text(if (newCatName.isNotBlank()) "Thêm danh mục" else "Đóng")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCategoryDialog = false }) { Text("Đóng") }
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(VcnSoftGray)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 14.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Cá nhân",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = VcnTextPrimary
                )
                Surface(
                    shape = CircleShape,
                    color = VcnCardSurface,
                    shadowElevation = 1.dp
                ) {
                    IconButton(
                        onClick = onOpenAuth,
                        modifier = Modifier.size(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Tài khoản",
                            tint = VcnTextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // User Profile Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = VcnCardSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { if (!isLoggedIn) onOpenAuth() }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(VcnOrange, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = displayName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = VcnTextPrimary
                            )
                            Text(
                                text = displayEmail,
                                style = MaterialTheme.typography.bodySmall,
                                color = VcnTextSecondary
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = VcnTextSecondary
                    )
                }
            }
        }

        // Ví và tài khoản
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ví và tài khoản",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = VcnTextPrimary
                )
                Text(
                    text = "Quản lý",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = VcnOrange,
                    modifier = Modifier.clickable { showAddWalletDialog = true }
                )
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = VcnCardSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Tổng số dư",
                            style = MaterialTheme.typography.bodySmall,
                            color = VcnTextSecondary
                        )
                        Text(
                            text = "${CategoryUiHelper.formatVnd(totalBalance)} >",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = VcnOrange
                        )
                    }
                    HorizontalDivider(color = VcnDividerGray, thickness = 0.8.dp)

                    financeViewModel.wallets.forEachIndexed { index, wallet ->
                        val visual = CategoryUiHelper.getWalletVisual(wallet.iconName)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { editingWallet = wallet }
                                .padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(visual.bgColor, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = visual.icon,
                                        contentDescription = null,
                                        tint = visual.tintColor,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = wallet.name ?: "Ví",
                                        fontWeight = FontWeight.SemiBold,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = VcnTextPrimary
                                    )
                                    Text(
                                        text = wallet.subtitle ?: "",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = VcnTextSecondary
                                    )
                                }
                            }
                            Text(
                                text = CategoryUiHelper.formatVnd(wallet.balance),
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodySmall,
                                color = VcnTextPrimary
                            )
                        }
                        if (index < financeViewModel.wallets.lastIndex) {
                            HorizontalDivider(color = VcnDividerGray, thickness = 0.8.dp)
                        }
                    }
                }
            }
        }

        // Danh mục mặc định
        item {
            Text(
                text = "Danh mục mặc định",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = VcnTextPrimary
            )
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = VcnCardSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                SettingsActionRow(
                    icon = Icons.Default.Category,
                    iconBg = PastelPurple,
                    iconTint = Color(0xFF8E24AA),
                    title = "Quản lý danh mục",
                    subtitle = "${financeViewModel.categories.size} danh mục chi tiêu & thu nhập",
                    onClick = { showCategoryDialog = true }
                )
            }
        }

        // Nhắc nhở & Tiện ích
        item {
            Text(
                text = "Nhắc nhở & Tiện ích",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = VcnTextPrimary
            )
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = VcnCardSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    // Toggle nhắc nhở
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(PastelOrange, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = null,
                                    tint = VcnOrange,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Nhắc nhở ghi chi tiêu",
                                    fontWeight = FontWeight.SemiBold,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = VcnTextPrimary
                                )
                                Text(
                                    text = "Hằng ngày lúc 20:30",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = VcnTextSecondary
                                )
                            }
                        }
                        Switch(
                            checked = financeViewModel.dailyReminderEnabled,
                            onCheckedChange = { financeViewModel.toggleDailyReminder(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = VcnOrange
                            )
                        )
                    }

                    HorizontalDivider(color = VcnDividerGray, thickness = 0.8.dp)

                    SettingsActionRow(
                        icon = Icons.Default.BarChart,
                        iconBg = PastelOrange,
                        iconTint = VcnOrange,
                        title = "Báo cáo chi tiêu",
                        subtitle = "Xem phân tích & thống kê tháng 11",
                        onClick = onOpenReports
                    )

                    HorizontalDivider(color = VcnDividerGray, thickness = 0.8.dp)

                    SettingsActionRow(
                        icon = Icons.Default.Print,
                        iconBg = PastelOrange,
                        iconTint = VcnOrange,
                        title = "Xem trước bản in",
                        subtitle = "Xuất báo cáo chi tiêu PDF / A4",
                        onClick = onOpenPrintPreview
                    )

                    HorizontalDivider(color = VcnDividerGray, thickness = 0.8.dp)

                    SettingsActionRow(
                        icon = Icons.Default.CalendarMonth,
                        iconBg = PastelOrange,
                        iconTint = VcnOrange,
                        title = "Kế hoạch tháng sau",
                        subtitle = "Dự toán ngân sách tháng 12/2025",
                        onClick = onOpenNextMonthPlan
                    )
                }
            }
        }

        // Cài đặt & Hỗ trợ
        item {
            Text(
                text = "Cài đặt & Hỗ trợ",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = VcnTextPrimary
            )
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = VcnCardSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    SettingsActionRow(
                        icon = Icons.Default.Language,
                        iconBg = PastelTeal,
                        iconTint = Color(0xFF00897B),
                        title = "Giao diện & Ngôn ngữ",
                        subtitle = "Sáng • Tiếng Việt (VND)",
                        onClick = { financeViewModel.showFeedback("Giao diện Sáng • Tiếng Việt (VND)") }
                    )
                    HorizontalDivider(color = VcnDividerGray, thickness = 0.8.dp)
                    SettingsActionRow(
                        icon = Icons.Default.Security,
                        iconBg = PastelBlue,
                        iconTint = Color(0xFF1E88E5),
                        title = "Bảo mật & Dữ liệu",
                        subtitle = if (isLoggedIn) "Đã đồng bộ tài khoản VCN" else "Chưa đăng nhập • Lưu cục bộ",
                        onClick = onOpenAuth
                    )
                    HorizontalDivider(color = VcnDividerGray, thickness = 0.8.dp)
                    SettingsActionRow(
                        icon = Icons.AutoMirrored.Filled.HelpOutline,
                        iconBg = PastelGray,
                        iconTint = VcnTextSecondary,
                        title = "Trợ giúp",
                        subtitle = "Câu hỏi thường gặp & Phản hồi",
                        onClick = { financeViewModel.showFeedback("VCN Quản lý chi tiêu cá nhân v1.0") }
                    )
                }
            }
        }

        // Đăng nhập / Đăng xuất
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (!isLoggedIn) {
                    Button(
                        onClick = onOpenAuth,
                        colors = ButtonDefaults.buttonColors(containerColor = VcnOrange, contentColor = Color.White),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text("Đăng nhập / Đăng ký tài khoản", fontWeight = FontWeight.Bold)
                    }
                }

                Surface(
                    color = VcnRedBg,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenLogout() }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 14.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = null,
                            tint = VcnRed,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Đăng xuất",
                            color = VcnRed,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Text(
                    text = "Phiên bản VCN 1.0.0 • Đồng bộ đám mây an toàn",
                    style = MaterialTheme.typography.labelSmall,
                    color = VcnTextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun SettingsActionRow(
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(iconBg, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyMedium,
                    color = VcnTextPrimary
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = VcnTextSecondary
                )
            }
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = VcnTextSecondary
        )
    }
}
