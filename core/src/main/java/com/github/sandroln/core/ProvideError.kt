package com.github.sandroln.core

interface ProvideError {

    fun error(message: String)

    object Empty : ProvideError {
        override fun error(message: String) = Unit
    }
}