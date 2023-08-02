package com.github.sandroln.kanbanboard.board.data

class BoardAllData(
    private val boardId: String,
    private val startListeningTickets: StartListenToTickets
) : AssigneeName {

    @Volatile
    private var count = 0

    private val members = mutableMapOf<String, String>()

    fun accept(userName: String, id: String) = synchronized(lock) {
        members[id] = userName
        if (members.size == count)
            startListeningTickets.startTicketsOf(boardId)
    }

    override fun name(assigneeId: String) = synchronized(lock) { members[assigneeId] ?: assigneeId }

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