package com.github.sandroln.kanbanboard.board.settings.presentation

import com.github.sandroln.core.Communication
import com.github.sandroln.kanbanboard.board.main.data.BoardUser

interface FoundUsersCommunication : Communication.Mutable<List<BoardUser>> {
    class Base : Communication.Regular<List<BoardUser>>(), FoundUsersCommunication
}