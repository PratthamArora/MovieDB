package com.pratthamarora.moviedb.data.repository

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

class NetworkState(val status: Status, val msg: String) {
    companion object {
         var LOADED: NetworkState = NetworkState(Status.SUCCESS, "Success")
        var LOADING: NetworkState = NetworkState(Status.RUNNING, "Loading")
         var ERROR: NetworkState= NetworkState(Status.FAILED, "Error occurred")
    }

}