package com.example.supabasesimpleproject.Presentation.Screens.MainSreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.supabasesimpleproject.Presentation.Screens.Components.TextFieldSearch
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.supabasesimpleproject.Domain.State.ResultState
import com.example.supabasesimpleproject.Presentation.Screens.Components.BookCard
import com.example.supabasesimpleproject.Presentation.Screens.Components.CategoryItem
import kotlinx.coroutines.runBlocking


/**
 * composable-функция MainScreen представляет основной экран приложения
 * */

@Composable
// navController: NavHostController:  Объект для навигации между экранами.
// mainViewModel: MainViewModel = viewModel() :  ViewModel, управляющий состоянием и логикой экрана входа.
fun MainScreen(navController: NavHostController, mainViewModel: MainViewModel = viewModel()) {
    // Состояние для хранения текста поиска
    val textSearch = remember { mutableStateOf("") }

    // Наблюдаемое состояние для списка категорий из ViewModel
    val categories = mainViewModel.categories.observeAsState(emptyList())

    // Наблюдаемое состояние для списка книг из ViewModel
    val books = mainViewModel.books.observeAsState(emptyList())

    // Состояние для хранения выбранной категории, изначально не выбрана (-1)
    val selectedCategory = remember { mutableIntStateOf(-1) }

    val resultState by mainViewModel.resultState.collectAsState() // использует collectAsState() для преобразования потока состояний (Flow<ResultState>) из ViewModel в состояние

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Компонент для ввода текста поиска
        TextFieldSearch(
            value = textSearch.value,
            onvaluechange = { newText -> // Обработчик изменения текста
                textSearch.value = newText // Обновляем состояние текста поиска
                mainViewModel.filterList(newText, selectedCategory.intValue) // Фильтруем список книг
            }
        )
        // Обработка различных состояний загрузки данных
        when (resultState) {
            is ResultState.Error ->
                Text(text = (resultState as ResultState.Error).message)

            ResultState.Initialized -> TODO()
            ResultState.Loading -> {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    CircularProgressIndicator()
                }
            }

            is ResultState.Success -> {
                LazyRow {
                    items(categories.value.indices.toList()) { index ->
                        CategoryItem(
                            category = categories.value[index].name,
                            isSelected = selectedCategory.intValue == categories.value[index].id,
                            onClick = {
                                selectedCategory.intValue = categories.value[index].id
                                mainViewModel.filterList(
                                    textSearch.value,
                                    selectedCategory.intValue
                                )
                            }
                        )
                    }
                }
                LazyColumn {
                    items(books.value) { it ->
                        BookCard(book = it) {
                            runBlocking { // Запускаем блокирующий код
                                mainViewModel.getUrlImage(it) // Получаем URL изображения для книги
                            }
                        }
                    }
                }
            }
        }
    }
}



