package com.fpinbo.app.network

import arrow.core.Either
import retrofit2.http.GET

interface Api {

    @GET("events/all.json")
    suspend fun events(): Either<Throwable,List<NetworkEvent>>
}
