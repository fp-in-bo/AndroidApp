package com.fpinbo.app.network

import arrow.core.left
import arrow.core.right
import arrow.fx.IO
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

class IOCallAdapter<R>(private val type: Type) : CallAdapter<R, IO<R>> {
    override fun adapt(call: Call<R>): IO<R> {
        return IO.cancellable { callback ->
            call.enqueue(object : Callback<R> {
                override fun onFailure(call: Call<R>, t: Throwable) {
                    callback(t.left())
                }

                override fun onResponse(call: Call<R>, response: Response<R>) {
                    callback(response.body()!!.right())
                }
            })
            IO { call.cancel() }
        }
    }

    override fun responseType(): Type = type
}