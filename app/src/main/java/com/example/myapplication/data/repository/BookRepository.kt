package  com.example.myapplication.data.repository

import com.example.myapplication.data.local.entities.BookEntity
import com.example.myapplication.model.Book
import kotlinx.coroutines.flow.Flow


interface BookRepository {
    suspend fun searchBooks(query: String): List<Book>
    suspend fun getBookById(id: String): Book?
    suspend fun insertBook(book: Book)
    suspend fun deleteBook(book: BookEntity)
    fun getAllBooks(): Flow<List<BookEntity>>
    suspend fun getBookDetails(bookId: String) : Book?

}