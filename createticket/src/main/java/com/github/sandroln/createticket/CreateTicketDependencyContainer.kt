package com.github.sandroln.createticket

import androidx.lifecycle.ViewModel
import com.github.sandroln.core.DependencyContainer
import com.github.sandroln.createticket.presentation.CreateTicketViewModel
import com.github.sandroln.openedboard.OpenedBoardCore

class CreateTicketDependencyContainer(
    private val core: OpenedBoardCore,
    private val other: DependencyContainer
) : DependencyContainer {

    override fun module(className: Class<out ViewModel>) =
        if (className == CreateTicketViewModel::class.java)
            CreateTicketModule(core)
        else
            other.module(className)
}