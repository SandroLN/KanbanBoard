package com.github.sandroln.kanbanboard.board.main.data

import com.github.sandroln.kanbanboard.login.data.UserProfileCloud

interface BoardAllData : Assignee.NameAndId {

    fun newMembers(boardMembersIds: Set<String>): List<String>

    fun accept(user: UserProfileCloud, id: String)

    class Base(
        private val boardMembersCommunication: BoardMembersCommunication.Update,
        private val boardId: String,
        private val startListeningTickets: StartListenToTickets
    ) : BoardAllData {

        @Volatile
        private var count = 0

        private val members = mutableMapOf<String, UserProfileCloud>()

        override fun accept(user: UserProfileCloud, id: String) = synchronized(lock) {
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

        override fun id(assigneeName: String): String = synchronized(lock) {
            members.forEach {
                if (it.value.name == assigneeName)
                    return@synchronized it.key
            }
            return ""
        }

        override fun newMembers(boardMembersIds: Set<String>): List<String> = synchronized(lock) {
            val result = boardMembersIds.filter { !members.containsKey(it) }
            count = result.size + members.size
            result
        }

        companion object {
            private val lock = Object()
        }
    }
}