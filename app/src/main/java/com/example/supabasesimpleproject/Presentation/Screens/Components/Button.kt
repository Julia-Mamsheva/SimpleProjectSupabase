package com.example.supabasesimpleproject.Presentation.Screens.Components

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Описывает композируемую функцию ButtonNavigation, которая создаёт настраиваемую кнопку
 * */

@Composable
fun ButtonNavigation(label: String, onClick:()-> Unit, ) {
    Button(
        onClick = {
            onClick()
        }, // Устанавливает действие, которое будет выполнено при нажатии на кнопку
        shape = RoundedCornerShape(15.dp), // Устанавливает форму кнопки с закруглёнными углами радиусом
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            containerColor = Color.Blue
        ), //Настраивает цвета кнопки
        modifier = Modifier
            .height(55.dp)
            .defaultMinSize(minWidth = 150.dp)
            .wrapContentWidth() //Эта цепочка модификаторов изменяет внешний вид и расположение кнопки
    ) {
        //Этот композируемый компонент отображает текст на кнопке
        Text(
            label,
            fontSize = 14.sp,
            color = Color.White,
            fontWeight = FontWeight.W600
        )
    }
}