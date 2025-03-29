package com.example.supabasesimpleproject.Presentation.Screens.DetailsBook

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.supabasesimpleproject.Domain.State.ResultState
import com.example.supabasesimpleproject.Presentation.Navigation.NavigationRoutes
import com.example.supabasesimpleproject.Presentation.Screens.Components.ButtonNavigation
import com.example.supabasesimpleproject.Presentation.Screens.Components.TextFieldDropDown
import com.example.supabasesimpleproject.Presentation.Screens.Components.TextFieldEdit
import com.example.supabasesimpleproject.R

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun DetailsBookScreen(
    navController: NavHostController,
    id: String,
    viewModel: DetailsBookViewModel = viewModel { DetailsBookViewModel(id) },
) {
    val bookState = viewModel.state
    var imageUrl by remember { mutableStateOf("") }

    val context = LocalContext.current

    val resultStateUpdate by viewModel.resultStateUpdate.collectAsState()
    val resultStateDelete by viewModel.resultStateDelete.collectAsState()
    val resultStateUpload by viewModel.resultStateUpload.collectAsState()


    when (resultStateUpload) {
        is ResultState.Error -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = (resultStateUpload as ResultState.Error).message)
            }
        }

        ResultState.Initialized -> {}
        ResultState.Loading -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(top = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is ResultState.Success -> {
            LazyColumn {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "Редактирование книги",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        val imageState = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(LocalContext.current).data(imageUrl)
                                .size(Size.ORIGINAL)
                                .build()
                        ).state

                        LaunchedEffect(bookState) {
                            imageUrl = viewModel.getUrlImage(bookState.id)
                        }
                        if (imageState is AsyncImagePainter.State.Loading) {
                            Box(
                                modifier = Modifier
                                    .size(200.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                        if (imageState is AsyncImagePainter.State.Error) {
                            Image(

                                painter = painterResource(R.drawable.book),
                                contentDescription = bookState.title,
                                modifier = Modifier
                                    .size(200.dp)
                                    .clip(RoundedCornerShape(8.dp)),

                                contentScale = ContentScale.Crop
                            )
                        }

                        if (imageState is AsyncImagePainter.State.Success) {
                            Box(
                                contentAlignment = Alignment.Center
                            ) {
                                Image(

                                    painter = imageState.painter,
                                    contentDescription = bookState.title,
                                    modifier = Modifier
                                        .size(200.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))


                        Text(
                            "Наименование книги:",
                            textAlign = TextAlign.Left,
                            style = MaterialTheme.typography.labelMedium
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        TextFieldEdit(
                            value = bookState.title,
                            onValueChanged = { viewModel.updateState(bookState.copy(title = it)) },
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        var expanded by remember { mutableStateOf(false) }
                        val selectedCategory =
                            viewModel.categories.value?.find { it.id == bookState.category }
                        Text(
                            "Категория книги:",
                            textAlign = TextAlign.Left,
                            style = MaterialTheme.typography.labelMedium
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Box {
                            // Создаем текстовое поле с выпадающим списком, где отображается имя выбранной категории
                            TextFieldDropDown(selectedCategory?.name ?: "Выберите категорию") {
                                // Устанавливаем состояние expanded (развернуто/свернуто) в зависимости от переданного параметра
                                expanded = it
                            }

                            // Создаем выпадающее меню
                            DropdownMenu(
                                expanded = expanded, // Указываем, развернуто ли меню
                                onDismissRequest = { expanded = false } // Закрываем меню при нажатии вне его
                            ) {
                                // Перебираем список категорий из viewModel
                                viewModel.categories.value!!.forEach { category ->
                                    // Создаем элемент выпадающего меню для каждой категории
                                    DropdownMenuItem(
                                        text = { Text(category.name) }, // Отображаем имя категории
                                        onClick = {
                                            // При нажатии на элемент устанавливаем выбранную категорию
                                            bookState.category = category.id
                                            expanded = false // Закрываем меню
                                        }
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Описание книги:",
                            textAlign = TextAlign.Left,
                            style = MaterialTheme.typography.labelMedium
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        TextFieldEdit(
                            value = bookState.description,
                            onValueChanged = { viewModel.updateState(bookState.copy(description = it)) }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            "Жанр книги:",
                            textAlign = TextAlign.Left,
                            style = MaterialTheme.typography.labelMedium
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        TextFieldEdit(
                            value = bookState.genre,
                            onValueChanged = { viewModel.updateState(bookState.copy(genre = it)) }
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        when (resultStateUpdate) {
                            is ResultState.Error -> {
                                Toast.makeText(
                                    context,
                                    (resultStateUpload as ResultState.Error).message,
                                    Toast.LENGTH_SHORT
                                )
                                ButtonNavigation(
                                    label = "Сохранить изменения",
                                    onClick = {
                                        viewModel.updateBook()
                                    }
                                )
                            }

                            ResultState.Initialized -> {
                                ButtonNavigation(
                                    label = "Сохранить изменения",
                                    onClick = {
                                        viewModel.updateBook()
                                    }
                                )
                            }

                            ResultState.Loading -> {
                                CircularProgressIndicator()
                            }

                            is ResultState.Success -> {
                                Toast.makeText(
                                    context,
                                    (resultStateUpload as ResultState.Success).message,
                                    Toast.LENGTH_SHORT
                                )
                                ButtonNavigation(
                                    label = "Сохранить изменения",
                                    onClick = {
                                        viewModel.updateBook()
                                    }
                                )
                            }
                        }
                        Spacer(Modifier.height(10.dp))
                        ButtonNavigation(
                            label = "Вернуться назад",
                            onClick = {
                                navController.navigate(NavigationRoutes.MAIN)
                                {
                                    popUpTo(NavigationRoutes.DETAILSBOOK) {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                        Spacer(Modifier.height(10.dp))
                        when (resultStateDelete) {
                            is ResultState.Error -> {
                                Toast.makeText(
                                    context,
                                    (resultStateUpload as ResultState.Error).message,
                                    Toast.LENGTH_SHORT
                                )
                                ButtonNavigation(
                                    label = "Удалить книгу",
                                    onClick = {
                                        viewModel.deleteBook()
                                    }
                                )
                            }

                            ResultState.Initialized -> {
                                ButtonNavigation(
                                    label = "Удалить книгу",
                                    onClick = {
                                        viewModel.deleteBook()
                                    }
                                )
                            }

                            ResultState.Loading -> {
                                CircularProgressIndicator()
                            }

                            is ResultState.Success -> {
                                Toast.makeText(
                                    context,
                                    (resultStateUpload as ResultState.Success).message,
                                    Toast.LENGTH_SHORT
                                )
                                navController.navigate(NavigationRoutes.MAIN)
                                {
                                    popUpTo(NavigationRoutes.DETAILSBOOK) {
                                        inclusive = true
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
    }
}
