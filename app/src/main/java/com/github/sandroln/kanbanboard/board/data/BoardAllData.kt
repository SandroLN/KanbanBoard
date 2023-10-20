package com.github.sandroln.kanbanboard.board.data

import com.github.sandroln.kanbanboard.core.Communication
import com.github.sandroln.kanbanboard.login.data.UserProfileCloud

class BoardAllData(
    private val boardMembersCommunication: BoardMembersCommunication.Update,
    private val boardId: String,
    private val startListeningTickets: StartListenToTickets
) : AssigneeName {

    @Volatile
    private var count = 0

    private val members = mutableMapOf<String, UserProfileCloud>()

    fun accept(user: UserProfileCloud, id: String) = synchronized(lock) {
        members[id] = user
        if (members.size == count) {
            startListeningTickets.startTicketsOf(boardId)
            boardMembersCommunication.map(members.map {
                BoardUser.Base(
                    it.key,
                    it.value.name,
                    it.value.mail
                )
            })
        }
    }

    override fun name(assigneeId: String) =
        synchronized(lock) { members[assigneeId]?.name ?: assigneeId }

    fun newMembers(boardMembersIds: Set<String>): List<String> = synchronized(lock) {
        val result = boardMembersIds.filter { !members.containsKey(it) }
        count = result.size + members.size
        result
    }

    companion object {
        private val lock = Object()
    }
}

interface AssigneeName {
    fun name(assigneeId: String): String
}

interface BoardUser {

    fun id(): String = ""
    fun name(): String = ""

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

interface BoardMembersCommunication {

    interface Update : Communication.Update<List<BoardUser>>
    interface Observe : Communication.Observe<List<BoardUser>>
    interface Mutable : Update, Observe
    class Base : Communication.Regular<List<BoardUser>>(), Mutable
}