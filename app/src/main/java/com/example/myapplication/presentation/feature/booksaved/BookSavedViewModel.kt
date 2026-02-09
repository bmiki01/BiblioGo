package  com.example.myapplication.presentation.feature.booksaved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.dao.BookDao
import com.example.myapplication.data.local.entities.BookEntity
import com.example.myapplication.data.repository.BookRepository
import com.example.myapplication.model.Book
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookSavedViewModel @Inject constructor(
    private val repository: BookRepository,
    private val bookDao : BookDao
) : ViewModel() {

    val savedBooks = repository.getAllBooks().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun insertBook(book: Book) {
        viewModelScope.launch {
            repository.insertBook(book)
        }
    }

    fun getBookByIdStream(id: String): Flow<BookEntity?> {
        return bookDao.getBookById2(id).map { it ?: null }
    }

    fun deleteBook(book: BookEntity) {
        viewModelScope.launch {
            repository.deleteBook(book)
        }
    }
    fun convertToEntity(book: Book): BookEntity {

        return BookEntity(
            id = book.id,
            title = book.title,
            authors = book.authors.takeIf { it.isNotEmpty() }?.joinToString(", "), //book.authors.toString(),//(if (book.authors.isEmpty()) null else book.authors).toString(),
            firstPublishYear = book.firstPublishYear,
            description = book.description,
            coverId = book.coverId,
            subjects = book.subjects?.takeIf { it.isNotEmpty() }?.joinToString(", ")//book.subjects.toString()
        )
    }
}