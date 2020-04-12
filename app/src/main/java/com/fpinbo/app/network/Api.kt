package com.fpinbo.app.network

import arrow.fx.IO
import retrofit2.http.GET

interface Api {

    @GET("events/all.json")
    fun events(): IO<List<NetworkEvent>>
}