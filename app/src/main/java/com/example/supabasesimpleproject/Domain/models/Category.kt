package com.example.supabasesimpleproject.Domain.models

import kotlinx.serialization.Serializable

/**
 * Data class Category с двумя свойствами, предназначенный для сериализации/десериализации (например, в JSON или другом формате)
 * */

@Serializable // Указывает компилятору, что этот класс может быть сериализован и десериализован
data class Category(
    val id: Int,
    val name: String
)