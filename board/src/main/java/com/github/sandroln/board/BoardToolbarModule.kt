package com.github.sandroln.board

import com.github.sandroln.board.presentation.BoardNavigation
import com.github.sandroln.board.presentation.BoardToolbarCommunication
import com.github.sandroln.board.presentation.BoardToolbarMapper
import com.github.sandroln.board.presentation.BoardToolbarViewModel
import com.github.sandroln.chosenboard.ChosenBoardCache
import com.github.sandroln.core.Core
import com.github.sandroln.core.Module

internal class BoardToolbarModule(
    private val core: Core,
    private val navigation: BoardNavigation
) : Module<BoardToolbarViewModel> {

    override fun viewModel() = BoardToolbarViewModel(
        BoardToolbarMapper(),
        BoardToolbarCommunication.Base(),
        navigation,
        ChosenBoardCache.Base(core.storage())
    )
}