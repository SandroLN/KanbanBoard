package com.github.sandroln.kanbanboard.core

import android.content.Context
import com.github.sandroln.boards.BoardsDependencyContainer
import com.github.sandroln.boardssettings.BoardSettingsDependencyContainer
import com.github.sandroln.core.DependencyContainer
import com.github.sandroln.core.NavigationCommunication
import com.github.sandroln.createboard.CreateBoardDependencyContainer
import com.github.sandroln.login.LoginDependencyContainer
import com.github.sandroln.profile.ProfileDependencyContainer

interface MakeDependencies {

    fun dependencies(): DependencyContainer

    class Base(private val context: Context) : MakeDependencies {

        override fun dependencies(): DependencyContainer {
            val navigation = NavigationCommunication.Base()
            val featuresNavigation = FeaturesNavigation.Base(navigation)
            val core = CoreImpl(featuresNavigation, context, navigation)
            val error = DependencyContainer.Error()
            val login = LoginDependencyContainer(core, featuresNavigation, error)
            val profile = ProfileDependencyContainer(core, login)
            val boards = BoardsDependencyContainer(core, featuresNavigation, profile)
            val createBoard = CreateBoardDependencyContainer(core, featuresNavigation, boards)
            val boardSettings = BoardSettingsDependencyContainer(core, createBoard)
            return BaseDependencyContainer(featuresNavigation, core, boardSettings)
        }
    }
}