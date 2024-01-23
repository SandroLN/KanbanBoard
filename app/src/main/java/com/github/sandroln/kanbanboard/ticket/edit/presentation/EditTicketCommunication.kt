package com.github.sandroln.kanbanboard.ticket.edit.presentation

interface EditTicketCommunication {

    interface Observe : com.github.sandroln.core.Communication.Observe<EditTicketCallback>

    interface Update : com.github.sandroln.core.Communication.Update<EditTicketCallback>

    interface Mutable : Observe, Update

    class Base : com.github.sandroln.core.Communication.Regular<EditTicketCallback>(), Mutable
}