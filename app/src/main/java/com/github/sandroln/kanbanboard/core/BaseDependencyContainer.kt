package com.github.sandroln.kanbanboard.core

import androidx.lifecycle.ViewModel
import com.github.sandroln.core.DependencyContainer
import com.github.sandroln.kanbanboard.main.MainModule
import com.github.sandroln.kanbanboard.main.MainViewModel
import com.github.sandroln.kanbanboard.ticket.edit.EditTicketModule
import com.github.sandroln.kanbanboard.ticket.edit.presentation.EditTicketViewModel

class BaseDependencyContainer(
    private val featuresNavigation: FeaturesNavigation,
    private val core: CoreImpl,
    private val dependencyContainer: DependencyContainer,
) : DependencyContainer {

    override fun module(className: Class<out ViewModel>) = when (className) {
        MainViewModel::class.java -> MainModule(core, featuresNavigation)
        EditTicketViewModel::class.java -> EditTicketModule(core)
        else -> dependencyContainer.module(className)
    }
}