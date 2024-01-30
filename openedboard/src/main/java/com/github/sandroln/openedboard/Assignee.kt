package com.github.sandroln.openedboard

interface Assignee {

    interface Id {
        fun id(assigneeName: String): String
    }

    interface Name {
        fun name(assigneeId: String): String
    }

    interface NameAndId : Id, Name
}