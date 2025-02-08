package com.example.supabasesimpleproject.Domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
/**
 * Data class Profile с четырьмя свойствами, предназначенный для сериализации/десериализации (например, в JSON или другом формате)
 * */

@Serializable // Указывает компилятору, что этот класс может быть сериализован и десериализован
data class Profile(
    val username:String,
    val surname:String,
    @SerialName("datebirth") //Аннотация указывает, что при сериализации/десериализации это свойство должно соответствовать ключу "datebirth" в формате данных (например, JSON)
    val dateBirth:String?,
    val image:String?
)