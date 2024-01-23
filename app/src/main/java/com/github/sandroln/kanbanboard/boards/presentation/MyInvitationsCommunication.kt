package com.github.sandroln.kanbanboard.boards.presentation

interface MyInvitationsCommunication : com.github.sandroln.core.Communication.Mutable<List<BoardInvitation>> {

    class Base : com.github.sandroln.core.Communication.Regular<List<BoardInvitation>>(), MyInvitationsCommunication
}