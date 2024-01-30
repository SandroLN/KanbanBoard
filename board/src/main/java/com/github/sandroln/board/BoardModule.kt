package com.github.sandroln.board

import com.github.sandroln.board.data.BoardCloudDataSource
import com.github.sandroln.board.data.BoardMembers
import com.github.sandroln.board.data.BoardRepository
import com.github.sandroln.board.data.InitBoardMapper
import com.github.sandroln.board.data.MoveTicketCloudDataSource
import com.github.sandroln.board.data.ProvideErrorToBoard
import com.github.sandroln.board.data.Tickets
import com.github.sandroln.board.data.UpdateBoard
import com.github.sandroln.board.presentation.BoardCommunication
import com.github.sandroln.board.presentation.BoardToTicketNavigation
import com.github.sandroln.board.presentation.BoardViewModel
import com.github.sandroln.board.presentation.ColumnTicketCommunication
import com.github.sandroln.board.presentation.TicketsCommunication
import com.github.sandroln.chosenboard.ChosenBoardCache
import com.github.sandroln.core.Core
import com.github.sandroln.core.Module
import com.github.sandroln.openedboard.EditTicketIdCache
import com.github.sandroln.openedboard.MemberName
import com.github.sandroln.openedboard.ProvideBoardScopeModule

internal class BoardModule(
    private val boardScopeModule: ProvideBoardScopeModule,
    private val core: Core,
    private val navigation: BoardToTicketNavigation
) : Module<BoardViewModel> {

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
        val boardCloudDataSource = BoardCloudDataSource.Base(
            boardScopeModule.boardScopeModule().provideContainer(),
            UpdateBoard.Base(communication, ticketsCommunication),
            Tickets.CloudDataSource.Base(handleError, core.service()),
            BoardMembers.CloudDataSource.Base(
                core.provideMyUser(),
                handleError,
                core.service()
            ),
            MemberName.CloudDataSource.Base(handleError, core.service())
        )
        return BoardViewModel(
            core.serialization(),
            BoardRepository.Base(
                InitBoardMapper(boardCloudDataSource),
                MoveTicketCloudDataSource.Base(core.service()),
                editTicketIdCache,
                ChosenBoardCache.Base(storage)
            ),
            ticketsCommunication,
            communication,
            navigation
        )
    }
}