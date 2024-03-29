package com.github.sandroln.board

import androidx.lifecycle.ViewModel
import com.github.sandroln.board.presentation.BoardNavigation
import com.github.sandroln.board.presentation.BoardToTicketNavigation
import com.github.sandroln.board.presentation.BoardToolbarViewModel
import com.github.sandroln.board.presentation.BoardViewModel
import com.github.sandroln.core.Core
import com.github.sandroln.core.DependencyContainer
import com.github.sandroln.openedboard.ProvideBoardScopeModule

class BoardDependencyContainer(
    private val boardScopeModule: ProvideBoardScopeModule,
    private val core: Core,
    private val navigation: BoardNavigationList,
    private val other: DependencyContainer
) : DependencyContainer {

    override fun module(className: Class<out ViewModel>) = when (className) {
        BoardViewModel::class.java -> BoardModule(boardScopeModule, core, navigation)
        BoardToolbarViewModel::class.java -> BoardToolbarModule(core, navigation)
        else -> other.module(className)
    }
}

interface BoardNavigationList : BoardToTicketNavigation, BoardNavigation