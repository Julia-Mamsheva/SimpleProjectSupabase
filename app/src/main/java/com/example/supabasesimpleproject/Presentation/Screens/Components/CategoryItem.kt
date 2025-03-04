package com.example.supabasesimpleproject.Presentation.Screens.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Композируемая функция в Jetpack Compose для создания элемента пользовательского интерфейса для отображения категории, который может быть выбран пользователем
 * */

@Composable
// Параметр category - Название категории, которое будет отображаться
// Параметр isSelected - Логическое значение, указывающее, выбрана ли категория. Это влияет на стиль элемента
// Параметр onClick - Функция обратного вызова, которая будет вызвана при нажатии на элемент
fun CategoryItem(category: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) Color(0xFF48B2E7) else Color.Transparent,
                shape = RoundedCornerShape(15.dp)
            )
            .clickable(onClick = onClick,
                indication = null, // Убираем стандартную индикацию нажатия
                interactionSource = remember { MutableInteractionSource() }) // Источник взаимодействия
            .background(Color.White, shape = RoundedCornerShape(15.dp))
            .padding(10.dp)
            .width(150.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = category, color = Color.Black, textAlign = TextAlign.Center)
    }
}