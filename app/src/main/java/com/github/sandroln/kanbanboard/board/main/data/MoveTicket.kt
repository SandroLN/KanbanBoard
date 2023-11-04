package com.github.sandroln.kanbanboard.board.main.data

import com.github.sandroln.kanbanboard.board.main.presentation.Column

interface MoveTicket {

    fun moveTicket(id: String, newColumn: Column)
}