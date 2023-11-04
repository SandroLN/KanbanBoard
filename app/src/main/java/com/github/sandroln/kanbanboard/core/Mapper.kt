package com.github.sandroln.kanbanboard.core

interface Mapper<S : Any, R : Any> {

    fun map(source: S): R

    interface Unit<S : Any> : Mapper<S, kotlin.Unit>
}