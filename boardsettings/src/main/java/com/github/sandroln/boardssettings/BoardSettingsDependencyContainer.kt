package com.github.sandroln.boardssettings

import androidx.lifecycle.ViewModel
import com.github.sandroln.boardssettings.presentation.BoardInvitationViewModel
import com.github.sandroln.boardssettings.presentation.BoardSettingsViewModel
import com.github.sandroln.core.Core
import com.github.sandroln.core.DependencyContainer
import com.github.sandroln.openedboard.ProvideBoardScopeModule

class BoardSettingsDependencyContainer(
    private val boardScopeModule: ProvideBoardScopeModule,
    private val core: Core,
    private val other: DependencyContainer
) : DependencyContainer {

    override fun module(className: Class<out ViewModel>) = when (className) {
        BoardSettingsViewModel::class.java -> BoardSettingsModule(boardScopeModule, core)
        BoardInvitationViewModel::class.java -> BoardInvitationModule(core)
        else -> other.module(className)
    }
}