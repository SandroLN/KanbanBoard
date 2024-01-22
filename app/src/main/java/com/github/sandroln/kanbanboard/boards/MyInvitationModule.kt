package com.github.sandroln.kanbanboard.boards

import com.github.sandroln.kanbanboard.boards.data.AcceptInvitationCloudDataSource
import com.github.sandroln.kanbanboard.boards.data.BoardCloudDataSource
import com.github.sandroln.kanbanboard.boards.data.Invitations
import com.github.sandroln.kanbanboard.boards.data.MyInvitationsRepository
import com.github.sandroln.kanbanboard.boards.data.RemoveInvitationCloudDataSource
import com.github.sandroln.kanbanboard.boards.presentation.MyInvitationsCommunication
import com.github.sandroln.kanbanboard.boards.presentation.MyInvitationsViewModel
import com.github.sandroln.kanbanboard.core.Core
import com.github.sandroln.kanbanboard.core.Module

class MyInvitationModule(private val core: Core) : Module<MyInvitationsViewModel> {

    override fun viewModel(): MyInvitationsViewModel {
        val service = core.service()
        return MyInvitationsViewModel(
            MyInvitationsRepository.Base(
                Invitations.CloudDataSource.Base(service),
                BoardCloudDataSource.Base(service),
                AcceptInvitationCloudDataSource.Base(service),
                RemoveInvitationCloudDataSource.Base(service),
                core.provideMyUser()
            ),
            MyInvitationsCommunication.Base()
        )
    }
}