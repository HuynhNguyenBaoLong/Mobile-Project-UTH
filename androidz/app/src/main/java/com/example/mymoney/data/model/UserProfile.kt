package com.example.mymoney.data.model

// OOP Model dữ liệu người dùng - Sử dụng dấu ? (nullable) an toàn dữ liệu
data class UserProfile(
    val uid: String? = null,
    val email: String? = null,
    val displayName: String? = null,
    val photoUrl: String? = null
)
