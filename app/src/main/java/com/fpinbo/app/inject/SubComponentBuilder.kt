package com.fpinbo.app.inject

interface SubComponentBuilder<T> {
    fun build() : T
}