package com.github.sandroln.boardssettings.presentation

import com.github.sandroln.core.Communication
import com.github.sandroln.openedboard.BoardUser

internal interface FoundUsersCommunication : Communication.Mutable<List<BoardUser>> {
    class Base : Communication.Regular<List<BoardUser>>(), FoundUsersCommunication
}