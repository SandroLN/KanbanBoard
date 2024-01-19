package com.github.sandroln.kanbanboard.core

interface ProvideError {

    fun error(message: String)

    object Empty : ProvideError {
        override fun error(message: String) = Unit
    }
}