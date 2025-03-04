package com.example.supabasesimpleproject.Presentation.Screens.MainSreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supabasesimpleproject.Domain.Constant.supabase
import com.example.supabasesimpleproject.Domain.State.ResultState
import com.example.supabasesimpleproject.Domain.models.Book
import com.example.supabasesimpleproject.Domain.models.Category
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainViewModel : ViewModel() {
    // MutableStateFlow, содержащий текущее ResultState. resultState обеспечивает доступ только для чтения к этому потоку состояний;
    // пользовательский интерфейс может собирать обновления из этого потока
    private val _resultState = MutableStateFlow<ResultState>(ResultState.Loading)
    val resultState: StateFlow<ResultState> = _resultState.asStateFlow()

    // MutableLiveData для хранения списка книг
    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>> get() = _books

    // MutableLiveData для хранения списка категорий
    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> get() = _categories

    // Хранит все книги для фильтрации
    private var allBooks: List<Book> = listOf()

    // Инициализация ViewModel, загрузка книг и категорий
    init {
        loadBooks()
        loadCategories()
    }

    // Функция для загрузки книг из базы данных
    private fun loadBooks() {
        _resultState.value = ResultState.Loading
        viewModelScope.launch {
            try {
                allBooks = supabase.postgrest.from("books").select().decodeList<Book>()
                _books.value = allBooks
                Log.d("MainBooks", "Success")
                Log.d("MainBooks", allBooks.toString())

                _resultState.value = ResultState.Success("Success")
            } catch (_ex: AuthRestException) {
                Log.d("MainBooks", _ex.message.toString())
                Log.d("MainBooks", _ex.errorCode.toString())
                Log.d("MainBooks", _ex.errorDescription)

                _resultState.value = ResultState.Error(_ex.errorDescription)
            }
        }
    }

    // Функция для получения URL изображения книги
    suspend fun getUrlImage(bookName: String): String {
        return withContext(Dispatchers.IO) {
            try {
                val url = supabase.storage.from("Books").publicUrl("${bookName}.png")
                Log.d("buck", url)
                url
            } catch (ex: AuthRestException) {
                Log.e("Error", "Failed to get URL: ${ex.message}")
                ""
            }
        }
    }

    // Функция для загрузки категорий из базы данных
    private fun loadCategories() {
        viewModelScope.launch {
            try {
                _categories.value = supabase.postgrest.from("categories").select().decodeList<Category>()
                Log.d("MainCategories", "Success")
                Log.d("MainCategories", _categories.toString())

            } catch (_ex: AuthRestException) {
                Log.d("Main", _ex.message.toString())
                Log.d("Main", _ex.errorCode.toString())
                Log.d("Main", _ex.errorDescription)
            }
        }
    }

    // Функция для фильтрации списка книг по запросу и выбранной категории
    fun filterList(query: String?, categoryId: Int?) {
        val filteredBooks = allBooks.filter { book ->
            val matchesTitle = query.isNullOrEmpty() || book.title.contains(query, ignoreCase = true) || book.description.contains(query, ignoreCase = true)
            val matchesCategory = categoryId == -1 || book.categoryId == categoryId
            matchesTitle && matchesCategory
        }
        _books.value = filteredBooks
    }
}