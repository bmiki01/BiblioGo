package  com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myapplication.presentation.feature.about.AboutScreen
import com.example.myapplication.presentation.feature.booklist.BookListScreen
import com.example.myapplication.presentation.feature.booksaved.BookSavedScreen
import com.example.myapplication.presentation.feature.booksearch.BookSearchScreen
import com.example.myapplication.presentation.feature.bookdeatails.BookDeatailsScreen
import com.example.myapplication.presentation.feature.booksaveddetail.BookSavedDetailScreen
import com.example.myapplication.presentation.feature.home.HomeScreen


@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(
                onBookListClick = { navController.navigate(Screen.BookList.route) },
                onSavedBooksClick = { navController.navigate(Screen.BookSaved.route) },
                onAboutClick = { navController.navigate(Screen.About.route) }
            )
        }

        composable(
            route = Screen.BookSavedDetail.route,
            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
        ) { navBackStackEntry ->
            val bookId = navBackStackEntry.arguments?.getString("bookId")!!
            BookSavedDetailScreen(bookId = bookId, navController = navController)
        }

        composable(route = Screen.BookList.route) {
            BookListScreen(
                navController = navController
                //onBookClick = { navController.navigate(Screen.BookDetails.route)}
            )
        }

        composable(
            route = Screen.BookDetails.route,
            arguments = listOf(
                androidx.navigation.navArgument("bookId") {
                    type = androidx.navigation.NavType.StringType
                    nullable = false
                }
            )
        ) { navBackStackEntry ->
            val bookId = navBackStackEntry.arguments?.getString("bookId")!!
            BookDeatailsScreen(bookId = bookId, navController = navController)
        }

        composable(route = Screen.BookSearch.route) {
            BookSearchScreen()
        }

        composable(route = Screen.BookSaved.route) {
            BookSavedScreen(navController = navController)
        }

        composable(route = Screen.About.route) {
            AboutScreen(navController = navController)
        }
    }
}