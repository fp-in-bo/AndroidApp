package com.fpinbo.app.network

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

class EitherCallAdapter<R>(private val type: Type) : CallAdapter<R, Call<Either<Throwable, R>>> {
    override fun adapt(call: Call<R>) = EitherCall(call)

    override fun responseType(): Type = type

    class EitherCall<R>(
        private val original: Call<R>,
    ) : Call<Either<Throwable, R>> {

        override fun enqueue(callback: Callback<Either<Throwable, R>>) {
            original.enqueue(object : Callback<R> {

                override fun onFailure(call: Call<R>, t: Throwable) {
                    callback.onResponse(this@EitherCall, Response.success(t.left()))
                }

                override fun onResponse(call: Call<R>, response: Response<R>) {
                    onResponse(
                        callback,
                        this@EitherCall,
                        response,
                        { body ->
                            Response.success(response.code(), body.right())
                        },
                        { errorBody ->
                            Response.success(errorBody.left())
                        }
                    )
                }
            })
        }

        override fun isExecuted(): Boolean = original.isExecuted

        override fun timeout(): Timeout = original.timeout()

        override fun clone(): Call<Either<Throwable, R>> = EitherCall(original.clone())

        override fun isCanceled(): Boolean = original.isCanceled

        override fun cancel() = original.cancel()

        override fun execute(): Response<Either<Throwable, R>> =
            throw UnsupportedOperationException("This adapter does not support sync execution")

        override fun request(): Request = original.request()

        internal inline fun <R, T> onResponse(
            callback: Callback<T>,
            call: Call<T>,
            response: Response<R>,
            okResponse: (R) -> Response<T>,
            failResponse: (Throwable) -> Response<T>
        ) {
            if (response.isSuccessful) {
                callback.onResponse(call, okResponse(response.body()!!))
            } else {
                val errorBody = response.errorBody()
                callback.onResponse(
                    call,
                    failResponse(Throwable(errorBody!!.string()))
                )

            }
        }
    }
}
