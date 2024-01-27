package com.github.sandroln.boards

import androidx.lifecycle.ViewModel
import com.github.sandroln.boards.main.BoardsModule
import com.github.sandroln.boards.main.BoardsNavigationList
import com.github.sandroln.boards.main.presentation.BoardsViewModel
import com.github.sandroln.boards.myinvitations.MyInvitationModule
import com.github.sandroln.boards.myinvitations.presentation.MyInvitationsViewModel
import com.github.sandroln.chosenboard.BoardCore
import com.github.sandroln.core.DependencyContainer
import com.github.sandroln.core.Module

class BoardsDependencyContainer(
    private val core: BoardCore,
    private val navigation: BoardsNavigationList,
    private val dependencyContainer: DependencyContainer
) : DependencyContainer {

    override fun module(className: Class<out ViewModel>): Module<out ViewModel> = when (className) {
        BoardsViewModel::class.java -> BoardsModule(navigation, core)
        MyInvitationsViewModel::class.java -> MyInvitationModule(core)
        else -> dependencyContainer.module(className)
    }
}