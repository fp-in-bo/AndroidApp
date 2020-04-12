package com.fpinbo.app.network

import arrow.fx.IO
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class IOAdapterFactory : CallAdapter.Factory() {

    companion object {
        fun create(): IOAdapterFactory = IOAdapterFactory()
    }

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        val rawType = getRawType(returnType)

        if (returnType !is ParameterizedType) {
            throw IllegalArgumentException("Return type must be parameterized")
        }

        val specifiedType = getParameterUpperBound(0, returnType)

        return if (rawType == IO::class.java) {
            IOCallAdapter<Type>(specifiedType)
        } else {
            null
        }


    }
}