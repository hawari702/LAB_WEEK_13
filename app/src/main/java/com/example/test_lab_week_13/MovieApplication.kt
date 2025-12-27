package com.example.test_lab_week_13

import android.app.Application
import com.example.test_lab_week_13.api.MovieService
import com.example.test_lab_week_13.database.MovieDatabase
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MovieApplication : Application() {

    // TODO: ganti dengan API key TMDB kamu
    private val apiKey: String = "4dddd41c0f7438ae8e32cae9574058fa"

    lateinit var movieRepository: MovieRepository
        private set

    override fun onCreate() {
        super.onCreate()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        val movieService = retrofit.create(MovieService::class.java)

        val movieDatabase = MovieDatabase.getInstance(this)

        movieRepository = MovieRepository(
            movieService = movieService,
            movieDatabase = movieDatabase,
            apiKey = apiKey
        )
    }
}
