package com.example.supabasesimpleproject.Domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class Book с пятью свойствами, предназначенный для сериализации/десериализации (например, в JSON или другом формате)
 * */

@Serializable // Указывает компилятору, что этот класс может быть сериализован и десериализован
data class Book(
    val id: String,
    val title: String,
    @SerialName("category_id") //Аннотация указывает, что при сериализации/десериализации это свойство должно соответствовать ключу "datebirth" в формате данных (например, JSON)
    val categoryId: Int,
    val description: String,
    val genre: String,
)
