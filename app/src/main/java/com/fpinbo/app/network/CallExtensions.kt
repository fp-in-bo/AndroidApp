package com.fpinbo.app.network

import arrow.core.left
import arrow.core.right
import arrow.fx.IO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun <T> Call<T>.toIO(): IO<T> {

    return IO.cancelable { callback ->

        enqueue(object : Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) = callback(t.left())

            override fun onResponse(call: Call<T>, response: Response<T>) =
                callback(response.body()!!.right())
        })

        IO { cancel() }
    }
}