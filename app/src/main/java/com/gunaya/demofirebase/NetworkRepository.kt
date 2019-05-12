package com.gunaya.demofirebase

import android.content.Context
import android.net.ConnectivityManager

interface NetworkStateRepository {
    fun isNetworkAvailable(): Boolean
}

class NetworkStateRepositoryImpl(private val context: Context) : NetworkStateRepository {
    override fun isNetworkAvailable(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }
}