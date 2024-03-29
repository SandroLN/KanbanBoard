package com.github.sandroln.board.data

import com.github.sandroln.common.UserProfileCloud
import com.github.sandroln.openedboard.ContainerBoardAllData
import com.github.sandroln.openedboard.MemberName
import com.github.sandroln.openedboard.StartListenToTickets
import com.github.sandroln.openedboard.TicketCloud


internal interface BoardCloudDataSource : StartListenToTickets, MemberName.Callback, BoardMembers.Callback,
    Tickets.Callback {

    fun init(boardId: String, isMyBoard: Boolean, ownerId: String)

    class Base(
        private val boardAllDataContainer: ContainerBoardAllData,
        private val updateBoard: UpdateBoard,
        private val ticketsCloudDataSource: Tickets.CloudDataSource,
        private val boardMembersCloudDataSource: BoardMembers.CloudDataSource,
        private val memberNameCloudDataSource: MemberName.CloudDataSource,
    ) : BoardCloudDataSource {

        init {
            boardMembersCloudDataSource.init(this)
            memberNameCloudDataSource.init(this)
            ticketsCloudDataSource.init(this)
        }

        override fun provideAllMembersIds(members: Set<String>) =
            boardAllDataContainer.newMembers(members).forEach(memberNameCloudDataSource::handle)

        override fun provideMember(user: UserProfileCloud, userId: String) =
            boardAllDataContainer.accept(user, userId)

        override fun provideTickets(tickets: List<Pair<String, TicketCloud>>) =
            updateBoard.update(tickets, boardAllDataContainer)

        override fun init(boardId: String, isMyBoard: Boolean, ownerId: String) {
            boardAllDataContainer.init(boardId, this)
            boardMembersCloudDataSource.provideAllMembers(boardId, isMyBoard, ownerId)
        }

        override fun startTicketsOf(boardId: String) =
            ticketsCloudDataSource.ticketsForBoard(boardId)
    }
}