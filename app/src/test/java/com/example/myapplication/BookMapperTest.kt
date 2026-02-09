package com.example.myapplication

import com.example.myapplication.data.local.entities.BookEntity
import com.example.myapplication.model.Book
import com.example.myapplication.presentation.feature.booksaved.toBook
import org.junit.Test

import org.junit.Assert.*

class BookMapperTest {

    @Test
    fun mapBookEntityToBook(){
        val entity = BookEntity(
            id = "OL123W",
            title = "Android Development",
            authors = "John Doe, Jane Smith",
            firstPublishYear = 2020,
            coverId = 123456,
            description = "An introduction to Android using Kotlin and Compose.",
            subjects = "Android, Programming"
        )

        val book = entity.toBook()

        assertEquals("OL123W", book.id)
        assertEquals("Android Development", book.title)
        assertEquals(listOf("John Doe", "Jane Smith"), book.authors)
        assertEquals(2020, book.firstPublishYear)
        assertEquals(123456, book.coverId)
        assertTrue(book.description!!.contains("Compose"))

    }

}