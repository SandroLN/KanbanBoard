package com.github.sandroln.boards.myinvitations.presentation

import com.github.sandroln.core.Communication

internal interface MyInvitationsCommunication : Communication.Mutable<List<BoardInvitation>> {

    class Base : Communication.Regular<List<BoardInvitation>>(), MyInvitationsCommunication
}