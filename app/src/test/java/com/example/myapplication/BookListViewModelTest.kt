package com.example.myapplication

import android.util.Log
import androidx.compose.runtime.collectAsState
import com.example.myapplication.data.repository.BookRepository
import com.example.myapplication.model.Book
import com.example.myapplication.navigation.Screen.BookDetails
import com.example.myapplication.presentation.feature.booklist.BookListViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import java.io.IOException


class BookListViewModelTest {

    private lateinit var viewModel: BookListViewModel
    private lateinit var repository: BookRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.e(any(), any<String>()) } answers { 0 }
        every { Log.d(any(), any<String>()) } answers { 0 }
        every { Log.e(any(), any(), any()) } returns 0
        Dispatchers.setMain(StandardTestDispatcher())
        repository = mockk<BookRepository>()
        viewModel = BookListViewModel(repository)
    }

    @Test
    fun testSearchBooks_populatesBooks() = runTest {
        // Given
        val mockBook = Book(
            id = "OL123W",
            title = "Android Programming",
            authors = listOf("Jane Doe"),
            firstPublishYear = 2022,
            coverId = 123456,
            description = null,
            subjects = null
        )
        coEvery { repository.searchBooks("android") } returns listOf(mockBook)

        // When
        viewModel.setSearchQuery("android")

        delay(600)
        // Then
        assertEquals(listOf(mockBook), viewModel.books.value)
    }

    @Test
    fun loadingBooksUpdatesStateCorrectly() = runBlocking {
        coEvery { repository.searchBooks("android") } returns mockk<List<Book>>()

        viewModel.setSearchQuery("android")

        delay(100)
        val result = viewModel.books.value
        assertNotNull(result)
    }

    @Test
    fun `coverUrl returns correct URL when coverId is present`() {
        // Given
        val book = Book(
            id = "OL123W",
            title = "Android Programming",
            authors = listOf("Jane Doe"),
            firstPublishYear = 2022,
            coverId = 123456,
            description = null,
            subjects = null
        )

        // When
        val expectedUrl = "https://covers.openlibrary.org/b/id/123456.jpg"

        // Then
        assertNotNull(book.coverUrl)
        assertEquals(expectedUrl, book.coverUrl)
    }

    @Test
    fun `enrichBookWithDetails updates book with new description`() = runTest {
        val originalBook = Book(
            id = "OL123",
            title = "Test Book",
            authors = listOf("Author A"),
            firstPublishYear = 2000,
            coverId = 1,
            description = null,
            subjects = null,
            numberOfPages = null,
            languages = null
        )

        val fetchedDetails = originalBook.copy(description = "New enriched description")
        coEvery { repository.getBookDetails("OL123") } returns fetchedDetails

        val result = CompletableDeferred<Book>()

        viewModel.enrichBookWithDetails(originalBook) {
            result.complete(it)
        }

        val enrichedBook = result.await()

        assertEquals("New enriched description", enrichedBook.description)
        assertEquals(originalBook.id, enrichedBook.id)
    }

    @Test
    fun `enrichBookWithDetails returns original if API fails`() = runTest {
        // Given
        val mockBook = Book(
            id = "OL123W",
            title = "Android Programming",
            authors = listOf("Jane Doe"),
            firstPublishYear = null,
            coverId = null,
            numberOfPages = null,
            languages = null,
            description = "Basic info",
            subjects = null
        )

        coEvery { repository.getBookDetails("OL123") } throws Exception("API Error")

        var resultBook: Book? = null

        // When
        viewModel.enrichBookWithDetails(mockBook) { enriched ->
            resultBook = enriched
        }
        delay(600)


        // Then
        assertNotNull(resultBook)
        assertSame(mockBook, resultBook)
        assertEquals("Basic info", resultBook?.description)
    }

}