package com.example.test_lab_week_13

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.test_lab_week_13.api.MovieService
import com.example.test_lab_week_13.database.MovieDatabase
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class MovieApplication : Application() {

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

        // ✅ Constraints: hanya jalan kalau ada internet
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // ✅ Periodic Work: jalan tiap 1 jam
        val workRequest = PeriodicWorkRequestBuilder<MovieWorker>(1, TimeUnit.HOURS)
            .setConstraints(constraints)
            .addTag("movie-work")
            .build()

        // ✅ Unique biar tidak dobel enqueue tiap app dibuka
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "movie-work",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}
