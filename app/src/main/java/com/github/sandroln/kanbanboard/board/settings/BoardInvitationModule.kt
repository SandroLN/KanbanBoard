package com.github.sandroln.kanbanboard.board.settings

import com.github.sandroln.chosenboard.ChosenBoardCache
import com.github.sandroln.core.Core
import com.github.sandroln.core.Module
import com.github.sandroln.core.ProvideError
import com.github.sandroln.kanbanboard.board.main.data.MemberName
import com.github.sandroln.kanbanboard.board.settings.data.BoardInvitationRepository
import com.github.sandroln.kanbanboard.board.settings.data.InvitationMapper
import com.github.sandroln.kanbanboard.board.settings.data.Invitations
import com.github.sandroln.kanbanboard.board.settings.presentation.BoardInvitationViewModel
import com.github.sandroln.openedboard.BoardMembersCommunication

class BoardInvitationModule(private val core: Core) : Module<BoardInvitationViewModel> {

    override fun viewModel(): BoardInvitationViewModel {
        val communication = BoardMembersCommunication.Base()
        val invitationCloudDataSource = Invitations.CloudDataSource.Base(core.service())
        val repository = BoardInvitationRepository.Base(
            InvitationMapper(invitationCloudDataSource),
            invitationCloudDataSource,
            MemberName.CloudDataSource.Base(ProvideError.Empty, core.service()),
            ChosenBoardCache.Base(core.storage())
        )
        return BoardInvitationViewModel(repository, communication)
    }
}