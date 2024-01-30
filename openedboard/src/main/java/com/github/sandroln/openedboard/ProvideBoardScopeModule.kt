package com.github.sandroln.openedboard

import com.github.sandroln.core.Core

interface ProvideBoardScopeModule {

    fun boardScopeModule(): BoardScopeModule
}

interface OpenedBoardCore : Core, ProvideBoardScopeModule