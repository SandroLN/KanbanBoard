package com.github.sandroln.boardssettings

import androidx.lifecycle.ViewModel
import com.github.sandroln.boardssettings.presentation.BoardInvitationViewModel
import com.github.sandroln.boardssettings.presentation.BoardSettingsViewModel
import com.github.sandroln.core.DependencyContainer
import com.github.sandroln.openedboard.OpenedBoardCore

class BoardSettingsDependencyContainer(
    private val core: OpenedBoardCore,
    private val other: DependencyContainer
) : DependencyContainer {

    override fun module(className: Class<out ViewModel>) = when (className) {
        BoardSettingsViewModel::class.java -> BoardSettingsModule(core)
        BoardInvitationViewModel::class.java -> BoardInvitationModule(core)
        else -> other.module(className)
    }
}