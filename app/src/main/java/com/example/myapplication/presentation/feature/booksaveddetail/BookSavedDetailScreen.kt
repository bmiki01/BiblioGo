package com.example.myapplication.presentation.feature.booksaveddetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.presentation.feature.booksaved.BookSavedViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myapplication.presentation.feature.booksaved.toBook
import com.example.myapplication.presentation.ui.theme.model.BookUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookSavedDetailScreen(
    bookId: String,
    viewModel: BookSavedViewModel = hiltViewModel(),
    navController: NavController
) {
    val bookEntity by viewModel.getBookByIdStream(bookId).collectAsState(initial = null)
    if (bookEntity == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading...")
        }
    } else {
        val book = bookEntity?.toBook()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Saved Book Details") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                        }
                    }
                )
            }
        ) { padding ->
            if (book != null) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                        .statusBarsPadding(),
                ) {

                    Spacer(modifier = Modifier.height(64.dp))

                    // Title
                    Text(
                        text = book.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    // Authors
                    if (book.authors.isNotEmpty()) {
                        Text(
                            text = "by ${book.authors.joinToString()}",
                            style = MaterialTheme.typography.titleMedium,
                            fontStyle = FontStyle.Italic
                        )
                    }

                    // Publish Year
                    book.firstPublishYear?.let {
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
                    if (!book.languages.isNullOrEmpty()) {
                        Text(
                            text = "Languages: ${book.languages.joinToString()}",
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
                    if (!book.subjects.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Subjects:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            } else {
                Text("No book found.")
            }
        }
    }

}
