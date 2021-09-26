package com.fpinbo.app.network

import arrow.core.Either
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class EitherAdapterFactory : CallAdapter.Factory() {

    companion object {
        fun create(): EitherAdapterFactory = EitherAdapterFactory()
    }

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        getRawType(returnType)

        if (returnType !is ParameterizedType) {
            throw IllegalArgumentException("Return type must be parameterized")
        }

        val eitherType = getParameterUpperBound(0, returnType)
        val specifiedType = getParameterUpperBound(1, eitherType as ParameterizedType)

        return if (getRawType(eitherType) == Either::class.java) {
            EitherCallAdapter<Type>(specifiedType)
        } else {
            null
        }


    }
}
