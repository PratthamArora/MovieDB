package com.pratthamarora.moviedb.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.pratthamarora.moviedb.data.api.GetMovieDetails
import com.pratthamarora.moviedb.data.model.Movie
import io.reactivex.disposables.CompositeDisposable

class MovieDataSourceFactory(
    private val apiService: GetMovieDetails,
    private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Int, Movie>() {

    val moviesLiveDataSource = MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val movieDataSource = MovieDataSource(apiService, compositeDisposable)

        moviesLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}