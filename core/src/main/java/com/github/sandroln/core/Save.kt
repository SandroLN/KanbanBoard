package com.github.sandroln.core

interface Save<T : Any> {
    fun save(data: T)
}