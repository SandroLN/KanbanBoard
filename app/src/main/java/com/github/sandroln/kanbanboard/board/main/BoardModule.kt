package com.github.sandroln.kanbanboard.board.main

import com.github.sandroln.kanbanboard.board.main.data.BoardCloudDataSource
import com.github.sandroln.kanbanboard.board.main.data.BoardMembers
import com.github.sandroln.kanbanboard.board.main.data.BoardRepository
import com.github.sandroln.kanbanboard.board.main.data.EditTicketIdCache
import com.github.sandroln.kanbanboard.board.main.data.MemberName
import com.github.sandroln.kanbanboard.board.main.data.MoveTicketCloudDataSource
import com.github.sandroln.kanbanboard.board.main.data.ProvideErrorToBoard
import com.github.sandroln.kanbanboard.board.main.data.Tickets
import com.github.sandroln.kanbanboard.board.main.data.UpdateBoard
import com.github.sandroln.kanbanboard.board.main.presentation.BoardCommunication
import com.github.sandroln.kanbanboard.board.main.presentation.BoardViewModel
import com.github.sandroln.kanbanboard.board.main.presentation.ColumnTicketCommunication
import com.github.sandroln.kanbanboard.board.main.presentation.TicketsCommunication
import com.github.sandroln.kanbanboard.boards.data.ChosenBoardCache
import com.github.sandroln.kanbanboard.core.Core
import com.github.sandroln.kanbanboard.core.Module

class BoardModule(private val core: Core) : Module<BoardViewModel> {

    override fun viewModel(): BoardViewModel {
        val ticketsCommunication = TicketsCommunication.Base(
            ColumnTicketCommunication.Base(),
            ColumnTicketCommunication.Base(),
            ColumnTicketCommunication.Base()
        )
        val communication = BoardCommunication.Base()
        val handleError = ProvideErrorToBoard(communication)
        val storage = core.storage()
        val editTicketIdCache = EditTicketIdCache.Base(storage)
        return BoardViewModel(
            core.serialization(),
            BoardRepository.Base(
                MoveTicketCloudDataSource.Base(core),
                BoardCloudDataSource.Base(
                    core.boardScopeModule().provideContainer(),
                    UpdateBoard.Base(communication, ticketsCommunication),
                    Tickets.CloudDataSource.Base(handleError, core),
                    BoardMembers.CloudDataSource.Base(core.provideMyUser(), handleError, core),
                    MemberName.CloudDataSource.Base(handleError, core)
                ),
                editTicketIdCache,
                ChosenBoardCache.Base(storage)
            ),
            ticketsCommunication,
            communication,
            core.navigation()
        )
    }
}