package com.github.sandroln.openedboard

interface MoveTickets<T : Any> {

    fun moveLeft(moving: T)

    fun moveRight(moving: T)
}