package com.pratthamarora.moviedb.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pratthamarora.moviedb.R
import com.pratthamarora.moviedb.data.api.POSTER_BASE_URL
import com.pratthamarora.moviedb.data.model.Movie
import com.pratthamarora.moviedb.data.repository.NetworkState
import com.pratthamarora.moviedb.ui.activity.MovieDetailActivity
import kotlinx.android.synthetic.main.movie_list_item.view.*
import kotlinx.android.synthetic.main.network_state_item.view.*


class PopularMoviePagedListAdapter(private val context: Context) :
    PagedListAdapter<Movie, RecyclerView.ViewHolder>(
        MovieDiffCallback()
    ) {

    private val MOVIE_VIEW_TYPE = 1
    private val NETWORK_VIEW_TYPE = 2

    private var networkState: NetworkState? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View

        return when (viewType) {
            MOVIE_VIEW_TYPE -> {
                view = layoutInflater.inflate(R.layout.movie_list_item, parent, false)
                MovieItemViewHolder(view)
            }
            else -> {
                view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
                NetworkStateItemViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (MOVIE_VIEW_TYPE) {
            getItemViewType(position) -> {
                (holder as MovieItemViewHolder).bind(getItem(position), context)
            }
            else -> {
                (holder as NetworkStateItemViewHolder).bind(networkState)
            }
        }
    }


    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            hasExtraRow() && position == itemCount - 1 -> {
                NETWORK_VIEW_TYPE
            }
            else -> {
                MOVIE_VIEW_TYPE
            }
        }
    }


    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }

    }


    class MovieItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(movie: Movie?, context: Context) {
            itemView.cv_movie_title.text = movie?.title
            itemView.cv_movie_release_date.text = movie?.releaseDate

            val moviePosterURL = POSTER_BASE_URL + movie?.posterPath
            Glide.with(itemView.context)
                .load(moviePosterURL)
                .into(itemView.cv_iv_movie_poster)

            itemView.setOnClickListener {
                val intent = Intent(context, MovieDetailActivity::class.java)
                intent.putExtra("id", movie?.id)
                context.startActivity(intent)
            }

        }

    }

    class NetworkStateItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(networkState: NetworkState?) {
            when {
                networkState != null && networkState == NetworkState.LOADING -> {
                    itemView.progress_bar_item.visibility = View.VISIBLE
                }
                else -> {
                    itemView.progress_bar_item.visibility = View.GONE
                }
            }

            when {
                networkState != null && networkState == NetworkState.ERROR -> {
                    itemView.error_msg_item.visibility = View.VISIBLE
                    itemView.error_msg_item.text = networkState.msg
                }
                networkState != null && networkState == NetworkState.END_OF_LIST -> {
                    itemView.error_msg_item.visibility = View.VISIBLE
                    itemView.error_msg_item.text = networkState.msg
                }
                else -> {
                    itemView.error_msg_item.visibility = View.GONE
                }
            }
        }
    }


    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        when {
            hadExtraRow != hasExtraRow -> {
                when {
                    hadExtraRow -> {
                        notifyItemRemoved(super.getItemCount())
                    }
                    else -> {
                        notifyItemInserted(super.getItemCount())
                    }
                }
            }
            hasExtraRow && previousState != newNetworkState -> {
                notifyItemChanged(itemCount - 1)
            }
        }

    }


}