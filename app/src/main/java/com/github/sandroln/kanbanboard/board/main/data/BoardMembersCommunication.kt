package com.github.sandroln.kanbanboard.board.main.data

interface BoardMembersCommunication {

    interface Update : com.github.sandroln.core.Communication.Update<List<BoardUser>>
    interface Observe : com.github.sandroln.core.Communication.Observe<List<BoardUser>>
    interface Mutable : Update, Observe
    class Base : com.github.sandroln.core.Communication.Regular<List<BoardUser>>(), Mutable
}