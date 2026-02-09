package  com.example.myapplication.data.remote.model

data class BookDto(
    val docs: List<BookDocDto>
)

data class BookDocDto(
    val key: String?, // This will be used as ID, e.g., "/works/OL12345W"
    val title: String?,
    val author_name: List<String>?,
    val first_publish_year: Int?,
    val number_of_pages: Int?,
    val language: List<String>?,//List<Map<String, String>>?,
    val cover_i: Int?,
    val description: Any?,
    val subject: List<String>?
)