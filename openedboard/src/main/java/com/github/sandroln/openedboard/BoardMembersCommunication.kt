package com.github.sandroln.openedboard

import com.github.sandroln.core.Communication

interface BoardMembersCommunication {

    interface Update : Communication.Update<List<BoardUser>>
    interface Observe : Communication.Observe<List<BoardUser>>
    interface Mutable : Update, Observe
    class Base : Communication.Regular<List<BoardUser>>(), Mutable
}