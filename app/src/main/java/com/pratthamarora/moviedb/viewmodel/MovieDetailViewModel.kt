package com.pratthamarora.moviedb.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pratthamarora.moviedb.data.model.MovieDetails
import com.pratthamarora.moviedb.data.repository.MovieDetailsRepository
import com.pratthamarora.moviedb.data.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MovieDetailViewModel(private val movieRepository: MovieDetailsRepository, movieId: Int) :
    ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val movieDetails: LiveData<MovieDetails> by lazy {
        movieRepository.fetchSingleMovieDetails(compositeDisposable, movieId)
    }

    val networkState: LiveData<NetworkState> by lazy {
        movieRepository.getMovieDetailsNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }


}