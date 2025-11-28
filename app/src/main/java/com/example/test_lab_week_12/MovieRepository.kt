package com.example.test_lab_week_12

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.test_lab_week_12.model.Movie
import com.example.test_lab_week_12.model.PopularMoviesResponse
import com.example.test_lab_week_12.api.MovieService

class MovieRepository(private val movieService: MovieService) {

    private val apiKey = "cc1857bd9db73b22732b28dec0c41542"

    private val movieLiveData = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>>
        get() = movieLiveData

    private val errorLiveData = MutableLiveData<String>()
    val error: LiveData<String>
        get() = errorLiveData

    suspend fun fetchMovies() {
        try {
            val popularMovies = movieService.getPopularMovies(apiKey)
            movieLiveData.postValue(popularMovies.results)
        } catch (exception: Exception) {
            errorLiveData.postValue("An error occurred: ${exception.message}")
        }
    }
}
