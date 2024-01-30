package com.github.sandroln.openedboard

interface AssignUser {

    fun assign(user: BoardUser)

    object Empty : AssignUser {
        override fun assign(user: BoardUser) = Unit
    }
}