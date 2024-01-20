package com.github.sandroln.kanbanboard.boards.presentation

import com.github.sandroln.kanbanboard.boards.data.AcceptInvitationCloudDataSource
import com.github.sandroln.kanbanboard.boards.data.BoardCloudDataSource
import com.github.sandroln.kanbanboard.boards.data.Invitations
import com.github.sandroln.kanbanboard.boards.data.MyInvitationsRepository
import com.github.sandroln.kanbanboard.boards.data.RemoveInvitationCloudDataSource
import com.github.sandroln.kanbanboard.core.Core
import com.github.sandroln.kanbanboard.core.Module

class MyInvitationModule(private val core: Core) : Module<MyInvitationsViewModel> {

    override fun viewModel() = MyInvitationsViewModel(
        MyInvitationsRepository.Base(
            Invitations.CloudDataSource.Base(core),
            BoardCloudDataSource.Base(core),
            AcceptInvitationCloudDataSource.Base(core),
            RemoveInvitationCloudDataSource.Base(core),
            core.provideMyUser()
        ),
        MyInvitationsCommunication.Base()
    )
}