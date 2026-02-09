package  com.example.myapplication.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Book(
    val id: String,             // Pl. "/works/OL12345W"
    val title: String,
    val authors: List<String>,
    val firstPublishYear: Int? = null,
    val coverId: Int? = null,
    val numberOfPages: Int? = null,
    val languages: List<String>? = null,
    val description: String? = null,
    val subjects: List<String>? = null
) : Parcelable {
    val coverUrl: String?
        get() = coverId?.let { "https://covers.openlibrary.org/b/id/$it.jpg" }
}