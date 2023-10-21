package com.github.sandroln.kanbanboard.ticket.edit.presentation

import com.github.sandroln.kanbanboard.core.Communication

interface EditTicketCommunication {

    interface Observe : Communication.Observe<EditTicketCallback>

    interface Update : Communication.Update<EditTicketCallback>

    interface Mutable : Observe, Update

    class Base : Communication.Regular<EditTicketCallback>(), Mutable
}