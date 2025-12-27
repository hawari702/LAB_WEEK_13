package com.example.test_lab_week_13

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.test_lab_week_13.MovieApplication
class MovieWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return try {
            val movieRepository =
                (applicationContext as MovieApplication).movieRepository

            movieRepository.fetchMoviesFromNetwork()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
