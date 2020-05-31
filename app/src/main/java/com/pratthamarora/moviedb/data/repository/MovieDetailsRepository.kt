package com.pratthamarora.moviedb.data.repository

import androidx.lifecycle.LiveData
import com.pratthamarora.moviedb.data.api.GetMovieDetails
import com.pratthamarora.moviedb.data.model.MovieDetails
import io.reactivex.disposables.CompositeDisposable


class MovieDetailsRepository (private val apiService : GetMovieDetails) {

    lateinit var movieDetailsDataSource: MovieDetailsDataSource

    fun fetchSingleMovieDetails (compositeDisposable: CompositeDisposable, movieId: Int) : LiveData<MovieDetails> {

        movieDetailsDataSource = MovieDetailsDataSource(apiService,compositeDisposable)
        movieDetailsDataSource.fetchMovieDetails(movieId)

        return movieDetailsDataSource.downloadedMovieResponse

    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkState> {
        return movieDetailsDataSource.networkState
    }

}