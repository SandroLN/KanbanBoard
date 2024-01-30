package com.github.sandroln.boards.myinvitations

import com.github.sandroln.boards.myinvitations.data.AcceptInvitationCloudDataSource
import com.github.sandroln.boards.myinvitations.data.BoardCloudDataSource
import com.github.sandroln.boards.myinvitations.data.Invitations
import com.github.sandroln.boards.myinvitations.data.MyInvitationsRepository
import com.github.sandroln.boards.myinvitations.data.RemoveInvitationCloudDataSource
import com.github.sandroln.boards.myinvitations.presentation.MyInvitationsCommunication
import com.github.sandroln.boards.myinvitations.presentation.MyInvitationsViewModel
import com.github.sandroln.core.Core
import com.github.sandroln.core.Module

internal class MyInvitationModule(private val core: Core) : Module<MyInvitationsViewModel> {

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