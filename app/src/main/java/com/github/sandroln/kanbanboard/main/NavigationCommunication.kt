package com.github.sandroln.kanbanboard.main

import com.github.sandroln.kanbanboard.core.Communication

interface NavigationCommunication {

    interface Observe : Communication.Observe<Screen>

    interface Update : Communication.Update<Screen>

    interface Mutable : Observe, Update

    class Base : Communication.Single<Screen>(), Mutable
}