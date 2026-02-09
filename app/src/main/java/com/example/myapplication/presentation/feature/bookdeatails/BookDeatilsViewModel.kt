package  com.example.myapplication.presentation.feature.bookdeatails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.BookRepository
import com.example.myapplication.model.Book
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDeatailsViewModel @Inject constructor(
    private val repository: BookRepository
) : ViewModel() {

    private val _book = MutableStateFlow<Book?>(null)
    val book = _book.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun loadBook(bookId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val book = repository.getBookById(bookId)
                _book.value = book
            } catch (e: Exception) {
                _error.value = "Failed to load book details"
                Log.e("BookDetailsViewModel", "Error: ${e.localizedMessage}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}