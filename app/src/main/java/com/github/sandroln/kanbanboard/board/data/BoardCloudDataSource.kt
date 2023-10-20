package com.github.sandroln.kanbanboard.board.data

import com.github.sandroln.kanbanboard.board.presentation.BoardCommunication
import com.github.sandroln.kanbanboard.board.presentation.BoardUiState
import com.github.sandroln.kanbanboard.board.presentation.Column
import com.github.sandroln.kanbanboard.board.presentation.TicketsCommunication
import com.github.sandroln.kanbanboard.login.data.UserProfileCloud

interface BoardCloudDataSource : StartListenToTickets, MemberName.Callback, BoardMembers.Callback,
    Tickets.Callback {

    fun init(boardId: String, isMyBoard: Boolean, ownerId: String)

    class Base(
        private val boardMembersCommunication: BoardMembersCommunication.Update,
        private val ticketsCloudDataSource: Tickets.CloudDataSource,
        private val boardMembersCloudDataSource: BoardMembers.CloudDataSource,
        private val memberNameCloudDataSource: MemberName.CloudDataSource,
        private val boardCommunication: BoardCommunication,
        private val ticketsCommunication: TicketsCommunication.Update
    ) : BoardCloudDataSource {

        private lateinit var boardAllData: BoardAllData

        init {
            boardMembersCloudDataSource.init(this)
            memberNameCloudDataSource.init(this)
            ticketsCloudDataSource.init(this)
        }

        override fun provideAllMembersIds(members: Set<String>) {
            boardAllData.newMembers(members).forEach(memberNameCloudDataSource::handle)
        }

        override fun provideMember(user: UserProfileCloud, userId: String) =
            boardAllData.accept(user, userId)

        override fun provideTickets(tickets: List<Pair<String, TicketCloud>>) =
            with(ticketsCommunication) {
                boardCommunication.map(BoardUiState.HideProgress)
                updateTodoTickets(data(tickets, Column.Todo))
                updateInProgressTickets(data(tickets, Column.InProgress))
                updateDoneTickets(data(tickets, Column.Done))
            }

        private fun data(data: List<Pair<String, TicketCloud>>, column: Column) =
            data.filter { it.second.columnId == column.cloudValue() }.map { (id, cloud) ->
                cloud.toUi(id, column, boardAllData)
            }

        override fun init(boardId: String, isMyBoard: Boolean, ownerId: String) {
            boardAllData = BoardAllData(boardMembersCommunication, boardId, this)
            boardMembersCloudDataSource.provideAllMembers(boardId, isMyBoard, ownerId)
        }

        override fun startTicketsOf(boardId: String) =
            ticketsCloudDataSource.ticketsForBoard(boardId)
    }
}

interface StartListenToTickets {
    fun startTicketsOf(boardId: String)
}