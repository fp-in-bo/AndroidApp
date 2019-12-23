package com.fpinbo.app.network

import retrofit2.Call
import retrofit2.http.GET

interface Api {

    @GET("events/all.json")
    fun events(): Call<List<NetworkEvent>>
}