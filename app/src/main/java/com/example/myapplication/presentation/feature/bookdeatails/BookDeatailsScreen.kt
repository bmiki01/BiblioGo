package com.example.myapplication.presentation.feature.bookdeatails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.myapplication.model.Book
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.navigation.NavController
import com.example.myapplication.presentation.feature.booklist.BookListViewModel
import com.google.android.material.chip.Chip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDeatailsScreen(bookId: String, viewModel: BookDeatailsViewModel = hiltViewModel(), navController: NavController) {

    val book by viewModel.book.collectAsState()
    val loading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val selectedBook = navController.previousBackStackEntry?.savedStateHandle?.get<Book>("book")

    LaunchedEffect(Unit) {
        viewModel.loadBook(bookId)
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Book Description") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back to Home"
                    )
                }
            }
        )
    }) { paddingValues ->
        if (loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error loading book details", color = Color.Red)
            }
        } else {
            BookDetailContent(book = book, modifier = Modifier.padding(paddingValues), selectedBook = selectedBook)
        }
    }
}

@Composable
private fun BookDetailContent(book: Book?, modifier: Modifier = Modifier, selectedBook: Book?) {
    if (book == null) {
        Text("No book found.")
        return
    }
    if (selectedBook == null){
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Book Cover
        selectedBook.coverUrl?.let { url ->
            AsyncImage(
                model = url,
                contentDescription = "Cover",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(200.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Title
        Text(
            text = book.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        // Authors
        if (selectedBook.authors.isNotEmpty()) {
            Text(
                text = "by ${selectedBook.authors.joinToString()}",
                style = MaterialTheme.typography.titleMedium,
                fontStyle = FontStyle.Italic
            )
        }

        // Publish Year
        selectedBook.firstPublishYear?.let {
            Text(
                text = "First published: $it",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        // Number of Pages
        book.numberOfPages?.let {
            Text(
                text = "Pages: $it",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        // Languages
        if (!selectedBook.languages.isNullOrEmpty()) {
            Text(
                text = "Languages: ${selectedBook.languages.joinToString()}",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        // Description
        if (!book.description.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "About the Book:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = book.description,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Subjects
        if (!selectedBook.subjects.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Subjects:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}