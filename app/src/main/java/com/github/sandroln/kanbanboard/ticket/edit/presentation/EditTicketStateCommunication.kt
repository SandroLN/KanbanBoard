package com.github.sandroln.kanbanboard.ticket.edit.presentation

interface EditTicketStateCommunication : com.github.sandroln.core.Communication.Mutable<EditTicketUiState> {
    class Base : com.github.sandroln.core.Communication.Regular<EditTicketUiState>(), EditTicketStateCommunication
}