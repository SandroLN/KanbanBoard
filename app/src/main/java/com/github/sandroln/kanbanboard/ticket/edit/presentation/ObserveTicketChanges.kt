package com.github.sandroln.kanbanboard.ticket.edit.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

interface ObserveTicketChanges {

    fun observeTicketChanges(owner: LifecycleOwner, observer: Observer<EditTicketCallback>) = Unit
}