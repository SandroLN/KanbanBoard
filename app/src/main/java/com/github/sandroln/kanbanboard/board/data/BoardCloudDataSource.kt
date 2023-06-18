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

interface BoardCloudDataSource : MoveTicket {

    fun init(boardId: String, isMyBoard: Boolean, ownerId: String)

    class Base(
        private val provideDatabase: ProvideDatabase,
        private val boardCommunication: BoardCommunication,
        private val ticketsCommunication: TicketsCommunication.Update
    ) : BoardCloudDataSource, StartListenToTickets, ProvideError {

        private lateinit var boardAllData: BoardAllData

        override fun init(boardId: String, isMyBoard: Boolean, ownerId: String) {
            boardAllData = BoardAllData(boardId, this)
            val boardMembersIds = mutableSetOf<String>()
            boardMembersIds.add(if (isMyBoard) Firebase.auth.currentUser!!.uid else ownerId)

            val membersQuery = provideDatabase.database()
                .child("boards-members")
                .orderByChild("boardId")
                .equalTo(boardId)

            val membersListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val memberIds = snapshot.children.mapNotNull {
                        it.getValue(OtherBoardCloud::class.java)?.memberId
                    }
                    boardMembersIds.addAll(memberIds)
                    val newMembers = boardAllData.newMembers(boardMembersIds)
                    newMembers.forEach(::handleMemberId)
                }

                override fun onCancelled(error: DatabaseError) = error(error.message)
            }
            membersQuery.removeEventListener(membersListener)
            membersQuery.addValueEventListener(membersListener)
        }

        private fun handleMemberId(memberId: String) {
            val query = provideDatabase.database()
                .child("users")
                .orderByKey()
                .equalTo(memberId)

            val userListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userCloud = snapshot.children.firstNotNullOf {
                        Pair(
                            it.key!!,
                            it.getValue(UserProfileCloud::class.java)
                        )
                    }
                    boardAllData.accept(userCloud.second!!.name, userCloud.first)
                }

                override fun onCancelled(error: DatabaseError) = error(error.message)
            }
            query.addListenerForSingleValueEvent(userListener)
        }

        private val ticketsListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                boardCommunication.map(BoardUiState.HideProgress)
                val data = snapshot.children.mapNotNull {
                    Pair(it.key!!, it.getValue(TicketCloud::class.java)!!)
                }

                ticketsCommunication.updateTodoTickets(data(data, Column.TODO, "todo"))
                ticketsCommunication.updateInProgressTickets(
                    data(data, Column.IN_PROGRESS, "inprogress")
                )
                ticketsCommunication.updateDoneTickets(data(data, Column.DONE, "done"))
            }

            private fun data(
                data: List<Pair<String, TicketCloud>>, column: Column, columnCloudId: String
            ) = data.filter { it.second.columnId == columnCloudId }.map { (id, cloud) ->
                cloud.toUi(id, column, boardAllData)
            }

            override fun onCancelled(error: DatabaseError) = error(error.message)
        }

        override fun startTicketsOf(boardId: String) {
            val query = provideDatabase.database()
                .child("tickets")
                .orderByChild("boardId")
                .equalTo(boardId)
            query.removeEventListener(ticketsListener)
            query.addValueEventListener(ticketsListener)
        }

        override fun error(message: String) = boardCommunication.map(BoardUiState.Error(message))

        override fun moveTicket(id: String, newColumn: Column) {
            val value = when (newColumn) {
                Column.TODO -> "todo"
                Column.IN_PROGRESS -> "inprogress"
                Column.DONE -> "done"
            }
            provideDatabase.database().child("tickets")
                .child(id)
                .child("columnId")
                .setValue(value)
        }
    }
}

private data class TicketCloud(
    val boardId: String = "",
    val columnId: String = "todo",
    val title: String = "",
    val assignee: String = "",
    val description: String = "",
    val color: String = "#EBC944"
) {
    fun toUi(id: String, column: Column, boardAllData: BoardAllData) = TicketUi(
        color, id, title, boardAllData.assignee(assignee), column
    )
}

private class BoardAllData(
    private val boardId: String,
    private val startListeningTickets: StartListenToTickets
) {

    @Volatile
    private var count = 0

    private val members = mutableMapOf<String, String>()

    fun accept(userName: String, id: String) = synchronized(lock) {
        members[id] = userName
        if (members.size == count)
            startListeningTickets.startTicketsOf(boardId)
    }

    fun assignee(assigneeId: String) = synchronized(lock) { members[assigneeId] ?: assigneeId }

    fun newMembers(boardMembersIds: MutableSet<String>): List<String> = synchronized(lock) {
        val result = boardMembersIds.filter { !members.containsKey(it) }
        count = result.size + members.size
        result
    }
}

private val lock = Object()

private interface StartListenToTickets {
    fun startTicketsOf(boardId: String)
}