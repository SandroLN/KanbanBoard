package com.github.sandroln.kanbanboard.board.main

import com.github.sandroln.chosenboard.ChosenBoardCache
import com.github.sandroln.core.Core
import com.github.sandroln.core.Module
import com.github.sandroln.kanbanboard.board.main.presentation.BoardToolbarCommunication
import com.github.sandroln.kanbanboard.board.main.presentation.BoardToolbarMapper
import com.github.sandroln.kanbanboard.board.main.presentation.BoardToolbarViewModel

class BoardToolbarModule(private val core: Core) : Module<BoardToolbarViewModel> {

    override fun viewModel() = BoardToolbarViewModel(
        BoardToolbarMapper(),
        BoardToolbarCommunication.Base(),
        core.navigation(),
        ChosenBoardCache.Base(core.storage())
    )
}