package com.example.mymoney.data.model

// OOP Model Danh mục thu chi - Thuộc tính dạng Nullable ?
data class CategoryItem(
    val id: String? = null,
    val name: String? = null,
    val isExpense: Boolean? = true,
    val iconName: String? = null
)
