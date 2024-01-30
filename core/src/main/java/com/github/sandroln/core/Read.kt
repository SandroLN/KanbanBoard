package com.github.sandroln.core

interface Read<T : Any> {

    fun read(): T
}