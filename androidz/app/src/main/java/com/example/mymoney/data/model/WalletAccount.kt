package com.example.mymoney.data.model

// OOP Model Ví & Tài khoản - Thuộc tính dạng Nullable ?
data class WalletAccount(
    val id: String? = null,
    val name: String? = null,
    val subtitle: String? = null,
    val balance: Double? = null,
    val iconName: String? = null
)
