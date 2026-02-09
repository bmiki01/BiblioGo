package  com.example.myapplication.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home_screen")
    object BookList : Screen("book_list_screen")
    object BookDetails : Screen("book_details_screen/{bookId}") {
        fun createRoute(bookId: String) = "book_details_screen/$bookId"
    }
    object BookSearch : Screen("book_search_screen")
    object BookSaved : Screen("book_saved_screen")
    object About : Screen("about_screen")
    object BookSavedDetail : Screen("book_saved_detail_screen/{bookId}"){
        fun createRoute(bookId: String) = "book_saved_detail_screen/$bookId"
    }
}