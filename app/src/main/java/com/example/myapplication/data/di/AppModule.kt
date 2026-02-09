package  com.example.myapplication.data.di

import com.example.myapplication.data.local.dao.BookDao
import com.example.myapplication.data.remote.api.BookApiService
import com.example.myapplication.data.repository.BookRepository
import com.example.myapplication.data.repository.BookRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBookRepository(
        api: BookApiService,
        dao: BookDao
    ): BookRepository = BookRepositoryImpl(api, dao)

}