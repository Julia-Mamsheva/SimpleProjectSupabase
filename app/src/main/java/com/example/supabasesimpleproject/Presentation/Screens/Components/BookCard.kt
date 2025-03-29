package com.example.supabasesimpleproject.Presentation.Screens.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.supabasesimpleproject.Domain.models.Book
import com.example.supabasesimpleproject.R

/**
 * Композируемая функция в Jetpack Compose для создания карточки для отображения информации о книге, включая изображение, заголовок и описание
 * */


@Composable
// Параметр book -  представляет собой объект типа Book. Это класс, который содержит информацию о книге
// Параметр getUrl - это функция, которая принимает объект типа Book и возвращает строку (String). Эта функция используется для получения URL-адреса изображения книги.
fun BookCard(book: Book, getUrl: (String) -> String, onClick:()->Unit) {
    // Состояние для хранения URL изображения книги
    var imageUrl by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                onClick()
            }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Создание состояния для загрузки изображения с использованием библиотеки Coil
            val imageState = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current).data(imageUrl)
                    .size(Size.ORIGINAL).build() // Запрос изображения с оригинальным размером
            ).state

            // Запускаем эффект, который обновляет imageUrl при изменении book
            LaunchedEffect(book) {
                imageUrl = getUrl(book.id)
            }
            // Проверяем состояние загрузки изображения
            if (imageState is AsyncImagePainter.State.Loading) {
                // Если изображение загружается, показываем индикатор загрузки
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    CircularProgressIndicator()
                }
            }
            // Если произошла ошибка при загрузке изображения, то ставим заглушку
            if (imageState is AsyncImagePainter.State.Error) {
                Image(

                    painter = painterResource(R.drawable.book),
                    contentDescription = book.title,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp)),

                    contentScale = ContentScale.Crop
                )
            }
            // Если изображение успешно загружено, используем загруженное изображение
            if (imageState is AsyncImagePainter.State.Success) {
                Image(

                    painter = imageState.painter,
                    contentDescription = book.title,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = book.title,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = book.description,
                    maxLines = 2, // Максимум 2 строки
                    overflow = TextOverflow.Ellipsis // Обрезка текста с многоточием
                )
            }
        }
    }
}