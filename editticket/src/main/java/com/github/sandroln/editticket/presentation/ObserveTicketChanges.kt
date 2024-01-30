package com.github.sandroln.editticket.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

internal interface ObserveTicketChanges {

    fun observeTicketChanges(owner: LifecycleOwner, observer: Observer<EditTicketCallback>) = Unit
}