package com.github.sandroln.kanbanboard.core

import androidx.lifecycle.ViewModel
import com.github.sandroln.kanbanboard.board.BoardModule
import com.github.sandroln.kanbanboard.board.presentation.BoardViewModel
import com.github.sandroln.kanbanboard.boards.BoardsModule
import com.github.sandroln.kanbanboard.boards.presentation.BoardsViewModel
import com.github.sandroln.kanbanboard.createboard.CreateBoardModule
import com.github.sandroln.kanbanboard.createboard.presentation.CreateBoardViewModel
import com.github.sandroln.kanbanboard.login.LoginModule
import com.github.sandroln.kanbanboard.login.presentation.LoginViewModel
import com.github.sandroln.kanbanboard.main.MainModule
import com.github.sandroln.kanbanboard.main.MainViewModel
import com.github.sandroln.kanbanboard.profile.ProfileModule
import com.github.sandroln.kanbanboard.profile.presentation.ProfileViewModel

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
            else -> dependencyContainer.module(className)
        }
    }
}