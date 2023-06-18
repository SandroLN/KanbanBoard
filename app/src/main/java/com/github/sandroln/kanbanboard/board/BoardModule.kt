package com.github.sandroln.kanbanboard.board

import com.github.sandroln.kanbanboard.board.data.BoardCloudDataSource
import com.github.sandroln.kanbanboard.board.data.BoardRepository
import com.github.sandroln.kanbanboard.board.data.EditTicketIdCache
import com.github.sandroln.kanbanboard.board.presentation.BoardCommunication
import com.github.sandroln.kanbanboard.board.presentation.BoardViewModel
import com.github.sandroln.kanbanboard.board.presentation.ColumnTicketCommunication
import com.github.sandroln.kanbanboard.board.presentation.TicketsCommunication
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
        return BoardViewModel(
            core.serialization(),
            BoardRepository.Base(
                BoardCloudDataSource.Base(
                    core,
                    communication,
                    ticketsCommunication
                ),
                EditTicketIdCache.Base(core.storage()),
                ChosenBoardCache.Base(core.storage())
            ),
            ticketsCommunication,
            communication,
            core.navigation()
        )
    }
}