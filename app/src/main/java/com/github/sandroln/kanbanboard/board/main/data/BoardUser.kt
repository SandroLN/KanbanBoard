package com.github.sandroln.kanbanboard.board.main.data

interface BoardUser {

    fun id(): String = ""

    fun name(): String = ""

    class Ui(private val name: String) : BoardUser {

        override fun name() = name
    }

    class Base(
        private val id: String,
        private val name: String,
        private val email: String
    ) : BoardUser {

        override fun id() = id

        override fun name() = name
    }

    object None : BoardUser
}