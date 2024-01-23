package com.github.sandroln.kanbanboard.board.main.data

import com.github.sandroln.kanbanboard.board.main.presentation.Column
import com.github.sandroln.kanbanboard.boards.data.ChosenBoardCache
import com.github.sandroln.kanbanboard.boards.presentation.BoardInfo

interface BoardRepository : MoveTicket, com.github.sandroln.core.SimpleInit {

    fun boardInfo(): BoardInfo

    fun saveTicketIdToEdit(id: String)

    class Base(
        private val moveTicketCloudDataSource: MoveTicketCloudDataSource,
        private val boardCloudDataSource: BoardCloudDataSource,
        private val editTicketIdCache: EditTicketIdCache.Save,
        private val chosenBoardCache: ChosenBoardCache.Read,
    ) : BoardRepository {

        override fun init() = boardInfo().init(boardCloudDataSource)

        override fun boardInfo() = chosenBoardCache.read()

        override fun saveTicketIdToEdit(id: String) = editTicketIdCache.save(id)

        override fun moveTicket(id: String, newColumn: Column) =
            moveTicketCloudDataSource.moveTicket(id, newColumn)
    }
}