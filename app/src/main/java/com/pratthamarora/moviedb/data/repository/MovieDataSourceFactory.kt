package com.oxcoding.moviemvvm.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.oxcoding.moviemvvm.data.vo.Movie
import com.pratthamarora.moviedb.data.api.GetMovieDetails
import io.reactivex.disposables.CompositeDisposable

class MovieDataSourceFactory (private val apiService : GetMovieDetails, private val compositeDisposable: CompositeDisposable)
    : DataSource.Factory<Int, Movie>() {

    val moviesLiveDataSource =  MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val movieDataSource = MovieDataSource(apiService,compositeDisposable)

        moviesLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}