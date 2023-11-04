package com.github.sandroln.kanbanboard.core

interface Read<T : Any> {

    fun read(): T
}