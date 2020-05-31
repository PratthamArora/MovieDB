package com.pratthamarora.moviedb.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.pratthamarora.moviedb.R
import com.pratthamarora.moviedb.data.api.GetMovieDetails
import com.pratthamarora.moviedb.data.api.MovieDbClient
import com.pratthamarora.moviedb.data.api.POSTER_BASE_URL
import com.pratthamarora.moviedb.data.model.MovieDetails
import com.pratthamarora.moviedb.data.repository.MovieDetailsRepository
import com.pratthamarora.moviedb.data.repository.NetworkState
import com.pratthamarora.moviedb.viewmodel.MovieDetailViewModel
import kotlinx.android.synthetic.main.activity_movie_detail.*
import java.text.NumberFormat
import java.util.*


class MovieDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: MovieDetailViewModel
    private lateinit var movieRepository: MovieDetailsRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        val movieId: Int = 299534

        val apiService: GetMovieDetails = MovieDbClient.getClient()
        movieRepository = MovieDetailsRepository(apiService)

        viewModel = getViewModel(movieId)

        viewModel.movieDetails.observe(this, Observer {
            setData(it)
        })

        viewModel.networkState.observe(this, Observer {
            progress_bar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE

        })

    }

    private fun setData(it: MovieDetails) {
        movie_title.text = it.title
        movie_tagline.text = it.tagline
        movie_release_date.text = it.releaseDate
        movie_rating.text = it.rating.toString()
        movie_runtime.text = it.runtime.toString() + " minutes"
        movie_overview.text = it.overview

        val numberFormat = NumberFormat.getCurrencyInstance(Locale.US)
        movie_budget.text = numberFormat.format(it.budget)
        movie_revenue.text = numberFormat.format(it.revenue)

        val moviePosterURL = POSTER_BASE_URL + it.posterPath
        Glide.with(this)
            .load(moviePosterURL)
            .into(iv_movie_poster)

    }


    private fun getViewModel(movieId: Int): MovieDetailViewModel {
        return ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MovieDetailViewModel(movieRepository, movieId) as T
            }
        })[MovieDetailViewModel::class.java]
    }
}