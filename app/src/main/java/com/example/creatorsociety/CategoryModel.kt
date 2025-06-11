package com.example.creatorsociety

data class CategoryModel(
    val name: String,
    val imageResId: Int,
    var isSelected: Boolean = false
)