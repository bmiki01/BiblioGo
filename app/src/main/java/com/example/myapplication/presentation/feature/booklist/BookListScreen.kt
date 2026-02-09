package  com.example.myapplication.presentation.feature.booklist

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.model.Book
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.myapplication.navigation.Screen
import com.example.myapplication.presentation.feature.booksaved.BookSavedViewModel
import com.example.myapplication.presentation.ui.theme.model.BookUiModel
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListScreen(
    viewModel: BookListViewModel = hiltViewModel(),
    navController: NavController,
    viewModel2: BookSavedViewModel = hiltViewModel()
) {
    val books by viewModel.books.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    //val savedBooks by BookSavedViewModel.savedBooks.collectAsState()
    val savedBooks by viewModel2.savedBooks.collectAsState()
    lateinit var analytics: FirebaseAnalytics
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Browse Books", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to Home",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black
                )
            )
        },
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ){
            Column(modifier = Modifier.fillMaxSize()) {
                // Search Bar
                TextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 60.dp)
                        .statusBarsPadding(),
                    label = { Text("Search for books...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    singleLine = true
                )

                // Book List
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(items = books) { book ->
                        BookUiModel(
                            book = book,
                            navController = navController,
                            isSaved = savedBooks.any {it.id == book.id},
                            onToggleFavorite = { book ->
                                analytics = Firebase.analytics
                                val bundle = Bundle().apply {
                                    putString("book_id", book.id)
                                    putString("book_title", book.title)
                                }
                                analytics.logEvent("book_saved", bundle)
                                if(savedBooks.any { it.id == book.id }){
                                    viewModel2.deleteBook(viewModel2.convertToEntity(book))
                                }else{
                                    viewModel.enrichBookWithDetails(book) { enrichedBook ->
                                        viewModel2.insertBook(enrichedBook)
                                    }
                                }
                            },
                            onClick = {
                                navController.currentBackStackEntry?.savedStateHandle?.set("book", book)
                                navController.navigate(Screen.BookDetails.createRoute(book.id))
                            }
                        )
                    }
                }
            }
        }}
    )
}
