package com.github.sandroln.kanbanboard.board.main.data

import com.github.sandroln.common.UserProfileCloud

interface ContainerBoardAllData :
    BoardAllData {

    fun init(
        boardId: String,
        startListeningTickets: StartListenToTickets
    )

    class Base(
        private val boardMembersCommunication: BoardMembersCommunication.Update,
    ) : ContainerBoardAllData {

        private lateinit var boardAllData: BoardAllData

        override fun init(boardId: String, startListeningTickets: StartListenToTickets) {
            boardAllData =
                BoardAllData.Base(boardMembersCommunication, boardId, startListeningTickets)
        }

        override fun newMembers(boardMembersIds: Set<String>) =
            boardAllData.newMembers(boardMembersIds)

        override fun accept(user: UserProfileCloud, id: String) = boardAllData.accept(user, id)

        override fun name(assigneeId: String) = boardAllData.name(assigneeId)

        override fun id(assigneeName: String) = boardAllData.id(assigneeName)
    }
}