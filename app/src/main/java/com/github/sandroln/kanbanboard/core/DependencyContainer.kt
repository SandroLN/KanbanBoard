package com.github.sandroln.kanbanboard.core

import androidx.lifecycle.ViewModel
import com.github.sandroln.kanbanboard.board.create.CreateBoardModule
import com.github.sandroln.kanbanboard.board.create.presentation.CreateBoardViewModel
import com.github.sandroln.kanbanboard.board.main.BoardModule
import com.github.sandroln.kanbanboard.board.main.BoardToolbarModule
import com.github.sandroln.kanbanboard.board.main.presentation.BoardToolbarViewModel
import com.github.sandroln.kanbanboard.board.main.presentation.BoardViewModel
import com.github.sandroln.kanbanboard.board.settings.BoardSettingsModule
import com.github.sandroln.kanbanboard.board.settings.presentation.BoardInvitationModule
import com.github.sandroln.kanbanboard.board.settings.presentation.BoardInvitationViewModel
import com.github.sandroln.kanbanboard.board.settings.presentation.BoardSettingsViewModel
import com.github.sandroln.kanbanboard.boards.BoardsModule
import com.github.sandroln.kanbanboard.boards.presentation.BoardsViewModel
import com.github.sandroln.kanbanboard.login.LoginModule
import com.github.sandroln.kanbanboard.login.presentation.LoginViewModel
import com.github.sandroln.kanbanboard.main.MainModule
import com.github.sandroln.kanbanboard.main.MainViewModel
import com.github.sandroln.kanbanboard.profile.ProfileModule
import com.github.sandroln.kanbanboard.profile.presentation.ProfileViewModel
import com.github.sandroln.kanbanboard.ticket.create.CreateTicketModule
import com.github.sandroln.kanbanboard.ticket.create.presentation.CreateTicketViewModel
import com.github.sandroln.kanbanboard.ticket.edit.EditTicketModule
import com.github.sandroln.kanbanboard.ticket.edit.presentation.EditTicketViewModel

interface DependencyContainer {

    fun module(className: Class<out ViewModel>): Module<out ViewModel>

    class Error : DependencyContainer {

        override fun module(className: Class<out ViewModel>): Module<out ViewModel> =
            throw IllegalArgumentException("unknown className $className")
    }

    class Base(
        private val core: Core,
        private val dependencyContainer: DependencyContainer = Error()
    ) : DependencyContainer {

        override fun module(className: Class<out ViewModel>) = when (className) {
            MainViewModel::class.java -> MainModule(core)
            LoginViewModel::class.java -> LoginModule(core)
            ProfileViewModel::class.java -> ProfileModule(core)
            BoardsViewModel::class.java -> BoardsModule(core)
            CreateBoardViewModel::class.java -> CreateBoardModule(core)
            BoardViewModel::class.java -> BoardModule(core)
            BoardToolbarViewModel::class.java -> BoardToolbarModule(core)
            CreateTicketViewModel::class.java -> CreateTicketModule(core)
            EditTicketViewModel::class.java -> EditTicketModule(core)
            BoardSettingsViewModel::class.java -> BoardSettingsModule(core)
            BoardInvitationViewModel::class.java -> BoardInvitationModule(core)
            else -> dependencyContainer.module(className)
        }
    }
}