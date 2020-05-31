package com.pratthamarora.moviedb.data.api

import com.pratthamarora.moviedb.data.model.MovieResponse
import com.pratthamarora.moviedb.data.model.MovieDetails
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GetMovieDetails {

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Single<MovieDetails>

    @GET("movie/popular")
    fun getPopularMovie(@Query("page") page: Int): Single<MovieResponse>
}