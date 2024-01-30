package com.github.sandroln.editticket.presentation

import com.github.sandroln.core.Communication

internal interface EditTicketStateCommunication : Communication.Mutable<EditTicketUiState> {
    class Base : Communication.Regular<EditTicketUiState>(), EditTicketStateCommunication
}