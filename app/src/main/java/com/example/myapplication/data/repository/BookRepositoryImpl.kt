package  com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.BookDao
import com.example.myapplication.data.local.entities.BookEntity
import com.example.myapplication.data.remote.api.BookApiService
import com.example.myapplication.data.remote.model.BookDocDto
import com.example.myapplication.model.Book
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class BookRepositoryImpl @Inject constructor(
    private val apiService: BookApiService,
    private val bookDao: BookDao
) : BookRepository {

    override suspend fun insertBook(book: Book) {
        bookDao.insertBook(convertToEntity(book))
    }

    override suspend fun deleteBook(book: BookEntity) {
        bookDao.deleteBook(book)
    }

    override fun getAllBooks(): Flow<List<BookEntity>> =
    bookDao.getAllBooks()

    fun convertToEntity(book: Book): BookEntity {

        //val authorList = if (book.authors.isNotEmpty()) book.authors else null

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

    fun mapDescription(description: Any?): String? {
        return when (description) {
            is String -> description
            is Map<*, *> -> description["value"] as? String
            else -> null
        }
    }

    override suspend fun searchBooks(query: String): List<Book> {
        val response = apiService.searchBooks(query)
        return response.docs.mapNotNull { doc ->
            val id = doc.key?.removePrefix("/works/") ?: return@mapNotNull null
            Book(
                id = id,
                title = doc.title ?: "No Title",
                authors = doc.author_name ?: emptyList(),
                firstPublishYear = doc.first_publish_year,
                numberOfPages = doc.number_of_pages,
                languages = doc.language ?: emptyList(),
                coverId = doc.cover_i,
                description = mapDescription(doc.description),//if (doc.description is String) doc.description else null,
                subjects = doc.subject
            )
        }
    }

    private fun mapBook(dto: BookDocDto): Book? {
        val id = dto.key?.removePrefix("/works/") ?: return null

        return Book(
            id = id,
            title = dto.title ?: "No Title",
            authors = dto.author_name ?: emptyList(),
            firstPublishYear = dto.first_publish_year,
            numberOfPages = dto.number_of_pages,
            languages = dto.language ?: emptyList(),
            description = mapDescription(dto.description),
            subjects = dto.subject,
            coverId = dto.cover_i
        )
    }

    override suspend fun getBookById(id: String): Book? {
        val response = apiService.getBookById("/works/$id")
        // Map from BookDto to Book
        return mapBook(response)
    }

    override suspend fun getBookDetails(bookId: String): Book? {
        val response = apiService.getBookDetails(bookId)
        return mapBook(response)
    }

}


