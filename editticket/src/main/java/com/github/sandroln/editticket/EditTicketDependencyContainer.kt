package com.github.sandroln.editticket

import androidx.lifecycle.ViewModel
import com.github.sandroln.core.Core
import com.github.sandroln.core.DependencyContainer
import com.github.sandroln.editticket.presentation.EditTicketViewModel
import com.github.sandroln.openedboard.ProvideBoardScopeModule

class EditTicketDependencyContainer(
    private val boardScopeModule: ProvideBoardScopeModule,
    private val core: Core,
    private val other: DependencyContainer
) : DependencyContainer {

    override fun module(className: Class<out ViewModel>) =
        if (className == EditTicketViewModel::class.java)
            EditTicketModule(boardScopeModule, core)
        else
            other.module(className)
}