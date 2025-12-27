package com.example.test_lab_week_13

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.test_lab_week_13.databinding.ActivityMainBinding
import com.example.test_lab_week_13.model.Movie

class MainActivity : AppCompatActivity() {

    private val movieAdapter by lazy {
        MovieAdapter(object : MovieAdapter.MovieClickListener {
            override fun onMovieClick(movie: Movie) {
                openMovieDetails(movie)
            }
        })
    }

    private lateinit var movieViewModel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.lifecycleOwner = this
        binding.movieList.adapter = movieAdapter

        val movieRepository =
            (application as MovieApplication).movieRepository

        movieViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return MovieViewModel(movieRepository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        )[MovieViewModel::class.java]

        // 🔥 INI PENTING
        binding.viewModel = movieViewModel
        binding.lifecycleOwner = this
    }

    private fun openMovieDetails(movie: Movie) {
        val intent = Intent(this, DetailsActivity::class.java).apply {
            putExtra(DetailsActivity.EXTRA_TITLE, movie.title)
            putExtra(DetailsActivity.EXTRA_RELEASE, movie.releaseDate)
            putExtra(DetailsActivity.EXTRA_OVERVIEW, movie.overview)
            putExtra(DetailsActivity.EXTRA_POSTER, movie.posterPath)
        }
        startActivity(intent)
    }
}
