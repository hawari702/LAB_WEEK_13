package com.example.test_lab_week_13

import android.util.Log
import com.example.test_lab_week_13.api.MovieService
import com.example.test_lab_week_13.database.MovieDatabase
import com.example.test_lab_week_13.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MovieRepository(
    private val movieService: MovieService,
    private val movieDatabase: MovieDatabase,
    private val apiKey: String
) {

    private val movieDao = movieDatabase.movieDao()

    // Fetch movies: kalau DB kosong ambil dari API lalu simpan; kalau ada pakai DB
    fun fetchMovies(): Flow<List<Movie>> = flow {
        val savedMovies = movieDao.getMoviesOnce()

        if (savedMovies.isEmpty()) {
            val movies = movieService.getPopularMovies(apiKey).results
            movieDao.addMovies(movies)
            emit(movies)
        } else {
            emit(savedMovies)
        }
    }.flowOn(Dispatchers.IO)

    fun getMoviesFromDb(): Flow<List<Movie>> = movieDao.getMoviesFlow()

    // ✅ Dipakai untuk refresh berkala (misal WorkManager)
    suspend fun fetchMoviesFromNetwork() {
        try {
            val moviesFetched = movieService.getPopularMovies(apiKey).results
            movieDao.addMovies(moviesFetched)
        } catch (e: Exception) {
            Log.d("MovieRepository", "An error occurred: ${e.message}")
        }
    }
}
