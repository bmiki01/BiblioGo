package  com.example.myapplication.data.remote.api

import com.example.myapplication.data.remote.model.BookDocDto
import com.example.myapplication.data.remote.model.BookDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface BookApiService {
    @GET("search.json")
    suspend fun searchBooks(@Query("q") query: String): BookDto

    @GET("{id}.json")
    suspend fun getBookById(@Path("id") id: String): BookDocDto

    @GET("/works/{id}.json")
    suspend fun getBookDetails(@Path("id") id: String): BookDocDto
}