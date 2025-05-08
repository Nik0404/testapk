package com.acroninspector.app.di.global.base

interface BaseComponent<V> {

    fun inject(view: V)
}