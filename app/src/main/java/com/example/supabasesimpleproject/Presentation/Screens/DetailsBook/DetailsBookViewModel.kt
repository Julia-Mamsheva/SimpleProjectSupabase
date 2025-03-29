package com.example.supabasesimpleproject.Presentation.Screens.DetailsBook

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supabasesimpleproject.Domain.Constant
import com.example.supabasesimpleproject.Domain.Constant.supabase
import com.example.supabasesimpleproject.Domain.State.BookState
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

class DetailsBookViewModel(id: String) : ViewModel() {

    var idBook = id

    // MutableStateFlow, содержащий текущее ResultState. resultState обеспечивает доступ только для чтения к этому потоку состояний;
    // пользовательский интерфейс может собирать обновления из этого потока
    private val _resultStateUpdate = MutableStateFlow<ResultState>(ResultState.Loading)
    val resultStateUpdate: StateFlow<ResultState> = _resultStateUpdate.asStateFlow()

    private val _resultStateDelete = MutableStateFlow<ResultState>(ResultState.Initialized)
    val resultStateDelete: StateFlow<ResultState> = _resultStateDelete.asStateFlow()

    private val _resultStateUpload = MutableStateFlow<ResultState>(ResultState.Initialized)
    val resultStateUpload: StateFlow<ResultState> = _resultStateUpload.asStateFlow()

    lateinit var book: Book

    // MutableLiveData для хранения списка категорий
    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> get() = _categories

    fun updateState(newState: BookState) {
        _state.value = newState
    }

    private val _state = mutableStateOf(BookState())
    val state: BookState get() = _state.value

    init {
        loadCategories()
        getBook()
    }

    fun getBook() {
        _resultStateUpload.value = ResultState.Loading
        viewModelScope.launch {
            try {
                book = supabase.postgrest.from("books").select() {
                    filter {
                        eq("id", idBook)
                    }
                }.decodeSingle<Book>()

                _state.value = BookState(
                    id = book.id,
                    title = book.title,
                    category = book.categoryId,
                    description = book.description,
                    genre = book.genre
                )
                _resultStateUpload.value = ResultState.Success("Success")
            } catch (e: AuthRestException) {
                Log.e("getBook", "Error loading data", e)
                _resultStateUpload.value = ResultState.Error(e.message.toString())
            }
        }
    }

    fun updateBook() {
        _resultStateUpdate.value = ResultState.Loading
        viewModelScope.launch {
            try {
                supabase.postgrest.from("books").update(
                    {
                        set("title", _state.value.title)
                        set("category_id", _state.value.category)
                        set("description", _state.value.description)
                        set("genre", _state.value.genre)
                    }
                ) {
                    filter {
                        eq("id", idBook)
                    }
                }
                _resultStateUpdate.value = ResultState.Success("Success")
            } catch (e: AuthRestException) {
                Log.e("updateBook", "Error update data", e)
                _resultStateUpdate.value = ResultState.Error(e.message.toString())
            }
        }
    }
    fun deleteBook()
    {
        _resultStateDelete.value = ResultState.Loading
        viewModelScope.launch {
            try {
                supabase.postgrest.from("books").delete(
                ) {
                    filter {
                        eq("id", idBook)
                    }
                }
                _resultStateDelete.value = ResultState.Success("Delete")
            } catch (e: AuthRestException) {
                Log.e("deleteBook", "Error delete data", e)
                _resultStateDelete.value = ResultState.Error(e.message.toString())
            }
        }
    }


    private fun loadCategories() {
        viewModelScope.launch {
            try {
                _categories.value =
                    supabase.postgrest.from("categories").select().decodeList<Category>()
                Log.d("loadCategories", "Success")
                Log.d("loadCategories", _categories.toString())

            } catch (_ex: AuthRestException) {
                Log.d("loadCategories", _ex.message.toString())
                Log.d("loadCategories", _ex.errorCode.toString())
                Log.d("loadCategories", _ex.errorDescription)
            }
        }
    }

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
}