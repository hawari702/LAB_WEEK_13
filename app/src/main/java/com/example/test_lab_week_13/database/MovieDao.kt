package com.example.test_lab_week_13.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.test_lab_week_13.model.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovies(movies: List<Movie>)

    // ✅ untuk cek sekali (dipakai fetchMovies)
    @Query("SELECT * FROM movies")
    suspend fun getMoviesOnce(): List<Movie>

    // ✅ untuk observe sebagai Flow (dipakai getMoviesFromDb)
    @Query("SELECT * FROM movies")
    fun getMoviesFlow(): Flow<List<Movie>>
}
