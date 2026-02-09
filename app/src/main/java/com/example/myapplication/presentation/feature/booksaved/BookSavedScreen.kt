package com.example.myapplication.presentation.feature.booksaved

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.myapplication.data.local.entities.BookEntity
import com.example.myapplication.model.Book
import com.example.myapplication.navigation.Screen
import com.example.myapplication.presentation.ui.theme.model.BookUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookSavedScreen(viewModel: BookSavedViewModel = hiltViewModel(), navController: NavController) {
    val books by viewModel.savedBooks.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Saved Books", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black
                )
            )
        },
        modifier = Modifier.statusBarsPadding()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ){
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(items = books) { bookEntity ->
                BookUiModel(
                    book = bookEntity.toBook(),
                    navController = navController,
                    isSaved = true,
                    onToggleFavorite = {
                        viewModel.deleteBook(bookEntity)
                    },
                    onClick = {
                        //navController.currentBackStackEntry?.savedStateHandle?.set("book", bookEntity)
                        navController.navigate(Screen.BookSavedDetail.createRoute(bookEntity.id))
                    }
                )
            }
        }}
    }
}

// Extension to convert BookEntity -> Book
fun BookEntity.toBook(): Book {
    val authorList = this.authors?.split(", ")?.filter { it.isNotBlank() } ?: emptyList()
    val subjectList = this.subjects?.split(", ")?.filter { it.isNotBlank() } ?: emptyList()

    return Book(
        id = this.id,
        title = this.title,
        authors = authorList,
        firstPublishYear = this.firstPublishYear,
        coverId = this.coverId,
        description = this.description,
        subjects = subjectList,
        numberOfPages = null,
        languages = null
    )
}