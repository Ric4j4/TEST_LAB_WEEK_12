package com.example.test_lab_week_12

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test_lab_week_12.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Calendar

class MovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    private val _popularMovies = MutableStateFlow(emptyList<Movie>())
    val popularMovies: StateFlow<List<Movie>> = _popularMovies

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    init {
        fetchPopularMovies()
    }

    private fun fetchPopularMovies() {
        viewModelScope.launch(Dispatchers.IO) {

            val currentYear = Calendar.getInstance().get(Calendar.YEAR).toString()

            movieRepository.fetchMovies()
                .map { list ->
                    list
                        .filter { it.releaseDate?.startsWith(currentYear) == true }
                        .sortedByDescending { it.popularity }
                }
                .catch { e ->
                    _error.value = "An exception occurred: ${e.message}"
                }
                .collect { filteredList ->
                    _popularMovies.value = filteredList
                }
        }
    }
}
