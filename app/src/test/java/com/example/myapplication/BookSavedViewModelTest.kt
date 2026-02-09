package com.example.myapplication

import android.util.Log
import com.example.myapplication.data.local.dao.BookDao
import com.example.myapplication.data.local.entities.BookEntity
import com.example.myapplication.data.repository.BookRepository
import com.example.myapplication.model.Book
import com.example.myapplication.presentation.feature.booklist.BookListViewModel
import com.example.myapplication.presentation.feature.booksaved.BookSavedViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before

import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mockito.verify
import org.mockito.verification.VerificationMode

class BookSavedViewModelTest {

    private lateinit var viewModel: BookSavedViewModel
    private lateinit var repository: BookRepository
    private lateinit var mockDao: BookDao

    private val testBook = Book(
        id = "123",
        title = "Sample Book",
        authors = listOf("John Doe"),
        firstPublishYear = 2021,
        coverId = 101,
        description = "Test description",
        subjects = listOf("Fiction"),
        numberOfPages = null,
        languages = null
    )

    private val testEntity = BookEntity(
        id = "123",
        title = "Sample Book",
        authors = "John Doe",
        firstPublishYear = 2021,
        description = "Test description",
        coverId = 101,
        subjects = "Fiction"
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.e(any(), any<String>()) } answers { 0 }
        every { Log.d(any(), any<String>()) } answers { 0 }
        Dispatchers.setMain(StandardTestDispatcher())
        repository = mockk<BookRepository>()
        every { repository.getAllBooks() } returns flowOf(listOf(testEntity))
        mockDao = mockk<BookDao>()
        viewModel = BookSavedViewModel(repository, mockDao)
    }

    @Test
    fun testConvertToEntity_convertsCorrectly() {
        // Given
        val book = Book(
            id = "OL123W",
            title = "Android Development",
            authors = listOf("Jane Doe", "John Smith"),
            firstPublishYear = 2022,
            coverId = 123456,
            description = "A complete guide to Android development.",
            subjects = listOf("Android", "Kotlin")
        )

        // When
        val entity = viewModel.convertToEntity(book)

        // Then
        assertEquals("OL123W", entity.id)
        assertEquals("Android Development", entity.title)
        assertEquals("Jane Doe, John Smith", entity.authors)
        assertEquals(2022, entity.firstPublishYear)
        assertEquals(123456, entity.coverId)
        assertTrue(entity.description!!.contains("development", ignoreCase = true))
        assertEquals("Android, Kotlin", entity.subjects)
    }

    @Test
    fun testGetBookByIdStream_returnsCorrectBookEntity() {
        // Given
        val expectedEntity = BookEntity(
            id = "OL123W",
            title = "Saved Book",
            authors = "Jane Doe",
            firstPublishYear = 2022,
            coverId = 123456,
            description = "This is a saved book",
            subjects = "Android, Compose"
        )

        every { mockDao.getBookById2("OL123W") } returns flowOf(expectedEntity)

        // When
        val resultFlow = viewModel.getBookByIdStream("OL123W")

        // Then
        runTest {
            val result = resultFlow.first()
            assertEquals(expectedEntity, result)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testInsertBook_callsRepositoryInsert() = runTest {
        // Given
        val book = Book(
            id = "OL789W",
            title = "Testing Guide",
            authors = listOf("Test Author"),
            firstPublishYear = 2023,
            coverId = 789012,
            description = "How to test Android apps.",
            subjects = listOf("Testing", "Compose")
        )
        coEvery { repository.insertBook(book) } just Runs
        // When
        viewModel.insertBook(book)

        delay(600)
        // Then – Use coVerify for suspend functions
        coVerify(exactly = 1) { repository.insertBook(book) }
    }

    @Test
    fun testDeleteBook_callsRepositoryDelete() = runTest {
        // Given
        val entity = BookEntity(
            id = "OL123W",
            title = "Old Book",
            authors = "Jane Doe",
            firstPublishYear = 2020,
            coverId = 123456,
            description = "Outdated book",
            subjects = "Android, Old"
        )
        coEvery { repository.deleteBook(entity) } just Runs
        // When
        viewModel.deleteBook(entity)
        delay(600)
        // Then
        coVerify(exactly = 1) { repository.deleteBook(entity) }
    }

}