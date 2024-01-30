package com.github.sandroln.kanbanboard.core

import com.github.sandroln.chosenboard.ClearBoardScopeModule
import com.github.sandroln.openedboard.BoardScopeModule
import com.github.sandroln.openedboard.ProvideBoardScopeModule

class BoardScopeModuleWrapper : ClearBoardScopeModule, ProvideBoardScopeModule {

    private var boardScopeModule: BoardScopeModule? = null

    override fun boardScopeModule(): BoardScopeModule {
        if (boardScopeModule == null)
            boardScopeModule = BoardScopeModule.Base()
        return boardScopeModule!!
    }

    override fun clearBoardScopeModule() {
        boardScopeModule = null
    }
}