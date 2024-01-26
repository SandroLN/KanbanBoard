package com.github.sandroln.kanbanboard.board.main.data

import com.github.sandroln.chosenboard.BoardCache
import com.github.sandroln.chosenboard.ChosenBoardCache
import com.github.sandroln.core.SimpleInit
import com.github.sandroln.kanbanboard.board.main.presentation.Column

interface BoardRepository : MoveTicket, SimpleInit {

    fun boardInfo(): BoardCache

    fun saveTicketIdToEdit(id: String)

    class Base(
        private val mapper: BoardCache.Mapper<Unit>,
        private val moveTicketCloudDataSource: MoveTicketCloudDataSource,
        private val editTicketIdCache: EditTicketIdCache.Save,
        private val chosenBoardCache: ChosenBoardCache.Read,
    ) : BoardRepository {

        override fun init() = boardInfo().map(mapper)

        override fun boardInfo() = chosenBoardCache.read()

        override fun saveTicketIdToEdit(id: String) = editTicketIdCache.save(id)

        override fun moveTicket(id: String, newColumn: Column) =
            moveTicketCloudDataSource.moveTicket(id, newColumn)
    }
}

class InitBoardMapper(
    private val boardCloudDataSource: BoardCloudDataSource,
) : BoardCache.Mapper<Unit> {

    override fun map(id: String, name: String, isMyBoard: Boolean, ownerId: String) =
        boardCloudDataSource.init(id, isMyBoard, ownerId)
}