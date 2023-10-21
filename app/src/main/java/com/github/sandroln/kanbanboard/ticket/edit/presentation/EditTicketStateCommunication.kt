package com.github.sandroln.kanbanboard.ticket.edit.presentation

import com.github.sandroln.kanbanboard.core.Communication

interface EditTicketStateCommunication : Communication.Mutable<EditTicketUiState> {
    class Base : Communication.Regular<EditTicketUiState>(), EditTicketStateCommunication
}