package com.example.mymoney.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

// Interface OOP định nghĩa giao diện chung cho 1 Tab
interface AppTabItem {
    val route: String
    val title: String
    val icon: ImageVector
}

// Sealed Class OOP áp dụng tính Kế Thừa và Đa Hình cho các Trang tab
sealed class AppTab(
    override val route: String,
    override val title: String,
    override val icon: ImageVector
) : AppTabItem {

    object Overview : AppTab(
        route = "overview",
        title = "Trang chủ",
        icon = Icons.Default.Home
    )

    object Transactions : AppTab(
        route = "transactions",
        title = "Giao dịch",
        icon = Icons.AutoMirrored.Filled.ListAlt
    )

    object AddTransaction : AppTab(
        route = "add_transaction",
        title = "",
        icon = Icons.Default.Add
    )

    object Budget : AppTab(
        route = "budget",
        title = "Ngân sách",
        icon = Icons.Default.AccountBalanceWallet
    )

    object Profile : AppTab(
        route = "profile",
        title = "Cá nhân",
        icon = Icons.Default.Person
    )
}

