package com.github.sandroln.kanbanboard.board.main.data

interface Assignee {

    interface Id {
        fun id(assigneeName: String): String
    }

    interface Name {
        fun name(assigneeId: String): String
    }

    interface NameAndId : Id, Name
}