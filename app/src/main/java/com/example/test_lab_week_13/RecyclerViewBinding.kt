package com.example.test_lab_week_13

import androidx.databinding.BindingAdapter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.example.test_lab_week_13.model.Movie
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@BindingAdapter("list")
fun bindMovies(view: RecyclerView, moviesFlow: StateFlow<List<Movie>>?) {
    val adapter = view.adapter as? MovieAdapter ?: return
    val lifecycleOwner = view.findViewTreeLifecycleOwner() ?: return

    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            moviesFlow?.collect { movies ->
                adapter.addMovies(movies)
            }
        }
    }
}
