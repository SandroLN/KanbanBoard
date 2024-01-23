package com.github.sandroln.kanbanboard.board.settings

import com.github.sandroln.core.Core
import com.github.sandroln.core.Module
import com.github.sandroln.core.ProvideError
import com.github.sandroln.kanbanboard.board.main.data.BoardMembersCommunication
import com.github.sandroln.kanbanboard.board.main.data.MemberName
import com.github.sandroln.kanbanboard.board.settings.data.BoardInvitationRepository
import com.github.sandroln.kanbanboard.board.settings.data.Invitations
import com.github.sandroln.kanbanboard.board.settings.presentation.BoardInvitationViewModel
import com.github.sandroln.kanbanboard.boards.data.ChosenBoardCache

class BoardInvitationModule(private val core: Core) :
    Module<BoardInvitationViewModel> {

    override fun viewModel(): BoardInvitationViewModel {
        val communication = BoardMembersCommunication.Base()
        val repository = BoardInvitationRepository.Base(
            Invitations.CloudDataSource.Base(core.service()),
            MemberName.CloudDataSource.Base(ProvideError.Empty, core.service()),
            ChosenBoardCache.Base(core.storage())
        )
        return BoardInvitationViewModel(repository, communication)
    }
}