package com.github.sandroln.editticket

import com.github.sandroln.core.Core
import com.github.sandroln.core.Module
import com.github.sandroln.editticket.data.ChangeTicketFields
import com.github.sandroln.editticket.data.EditTicketCloudDataSource
import com.github.sandroln.editticket.data.EditTicketRepository
import com.github.sandroln.editticket.presentation.EditTicketCommunication
import com.github.sandroln.editticket.presentation.EditTicketStateCommunication
import com.github.sandroln.editticket.presentation.EditTicketViewModel
import com.github.sandroln.openedboard.EditTicketIdCache
import com.github.sandroln.openedboard.ProvideBoardScopeModule

internal class EditTicketModule(
    private val boardScopeModule: ProvideBoardScopeModule,
    private val core: Core
) : Module<EditTicketViewModel> {

    override fun viewModel(): EditTicketViewModel {
        val editTicketCommunication = EditTicketCommunication.Base()
        val editTicketIdCache = EditTicketIdCache.Base(core.storage())
        val boardScopeModule = boardScopeModule.boardScopeModule()
        val assigneeNameId = boardScopeModule.provideContainer()
        val cloudDataSource =
            EditTicketCloudDataSource.Base(
                assigneeNameId,
                editTicketCommunication,
                core.service(),
                editTicketIdCache
            )
        return EditTicketViewModel(
            EditTicketStateCommunication.Base(),
            EditTicketRepository.Base(
                editTicketIdCache,
                ChangeTicketFields.Base(cloudDataSource, assigneeNameId),
                cloudDataSource
            ),
            boardScopeModule.provideMembers(),
            editTicketCommunication,
            core.navigation()
        )
    }
}