package com.github.sandroln.kanbanboard.board.data

import com.github.sandroln.kanbanboard.board.presentation.BoardCommunication
import com.github.sandroln.kanbanboard.board.presentation.BoardUiState
import com.github.sandroln.kanbanboard.board.presentation.Column
import com.github.sandroln.kanbanboard.board.presentation.TicketUi
import com.github.sandroln.kanbanboard.board.presentation.TicketsCommunication
import com.github.sandroln.kanbanboard.boards.data.OtherBoardCloud
import com.github.sandroln.kanbanboard.core.ProvideDatabase
import com.github.sandroln.kanbanboard.core.ProvideError
import com.github.sandroln.kanbanboard.login.data.UserProfileCloud
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

interface BoardCloudDataSource : StartListenToTickets, MemberName.Callback, BoardMembers.Callback,
    Tickets.Callback {

    fun init(boardId: String, isMyBoard: Boolean, ownerId: String)

    class Base(
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

        override fun provideMember(userName: String, userId: String) =
            boardAllData.accept(userName, userId)

        override fun provideTickets(tickets: List<Pair<String, TicketCloud>>) =
            with(ticketsCommunication) {
                boardCommunication.map(BoardUiState.HideProgress)
                updateTodoTickets(data(tickets, Column.TODO, "todo"))
                updateInProgressTickets(
                    data(tickets, Column.IN_PROGRESS, "inprogress")
                )
                updateDoneTickets(data(tickets, Column.DONE, "done"))
            }

        private fun data(
            data: List<Pair<String, TicketCloud>>, column: Column, columnCloudId: String
        ) = data.filter { it.second.columnId == columnCloudId }.map { (id, cloud) ->
            cloud.toUi(id, column, boardAllData)
        }

        override fun init(boardId: String, isMyBoard: Boolean, ownerId: String) {
            boardAllData = BoardAllData(boardId, this)
            boardMembersCloudDataSource.provideAllMembers(boardId, isMyBoard, ownerId)
        }

        override fun startTicketsOf(boardId: String) =
            ticketsCloudDataSource.ticketsForBoard(boardId)
    }
}

interface StartListenToTickets {
    fun startTicketsOf(boardId: String)
}