package com.github.sandroln.createticket

import androidx.lifecycle.ViewModel
import com.github.sandroln.core.Core
import com.github.sandroln.core.DependencyContainer
import com.github.sandroln.createticket.presentation.CreateTicketViewModel
import com.github.sandroln.openedboard.ProvideBoardScopeModule

class CreateTicketDependencyContainer(
    private val boardScopeModule: ProvideBoardScopeModule,
    private val core: Core,
    private val other: DependencyContainer
) : DependencyContainer {

    override fun module(className: Class<out ViewModel>) =
        if (className == CreateTicketViewModel::class.java)
            CreateTicketModule(boardScopeModule, core)
        else
            other.module(className)
}