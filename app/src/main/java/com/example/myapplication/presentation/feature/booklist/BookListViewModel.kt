package  com.example.myapplication.presentation.feature.booklist

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.remote.model.BookDocDto
import com.example.myapplication.data.remote.model.BookDto
import com.example.myapplication.data.repository.BookRepository
import com.example.myapplication.model.Book
import com.example.myapplication.presentation.ui.theme.model.BookUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BookListViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books = _books.asStateFlow()

    private val _searchQuery = MutableStateFlow("android")
    val searchQuery = _searchQuery.asStateFlow()

    fun enrichBookWithDetails(book: Book, onResult: (Book) -> Unit) {
        viewModelScope.launch {
            try {
                val details = bookRepository.getBookDetails(book.id)
                val enrichedBook = book.copy(description = details?.description ?: "")
                onResult(enrichedBook)
            } catch (e: Exception) {
                Log.e("BookListViewModel", "Failed to fetch details", e)
                onResult(book) // fallback: return original book
            }
        }
    }


    fun setSearchQuery(query: String){
        _searchQuery.value = query
    }

    init {
        loadBooks()
        viewModelScope.launch {
            _searchQuery
                .debounce(500)
                .collect { query ->
                    loadBooks()
                }
        }
    }

    private fun loadBooks(){
        viewModelScope.launch {
            try {
                val result = bookRepository.searchBooks(_searchQuery.value)
                _books.value = result
            } catch (e: Exception){
                Log.e("BookListViewModel", "Error: ${e.localizedMessage}")
            }
        }
    }

}