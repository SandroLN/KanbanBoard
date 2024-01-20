package com.github.sandroln.kanbanboard.boards.presentation

import com.github.sandroln.kanbanboard.core.Communication

interface MyInvitationsCommunication : Communication.Mutable<List<BoardInvitation>> {

    class Base : Communication.Regular<List<BoardInvitation>>(), MyInvitationsCommunication
}