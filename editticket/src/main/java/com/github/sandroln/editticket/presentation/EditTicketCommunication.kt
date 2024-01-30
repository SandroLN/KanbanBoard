package com.github.sandroln.editticket.presentation

import com.github.sandroln.core.Communication

internal interface EditTicketCommunication {

    interface Observe : Communication.Observe<EditTicketCallback>

    interface Update : Communication.Update<EditTicketCallback>

    interface Mutable : Observe, Update

    class Base : Communication.Regular<EditTicketCallback>(), Mutable
}