package com.github.sandroln.kanbanboard.core

interface Save<T : Any> {
    fun save(data: T)
}