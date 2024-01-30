package com.github.sandroln.kanbanboard.core

import android.content.Context
import com.github.sandroln.board.BoardDependencyContainer
import com.github.sandroln.boards.BoardsDependencyContainer
import com.github.sandroln.boardssettings.BoardSettingsDependencyContainer
import com.github.sandroln.cloudservice.MyUser
import com.github.sandroln.core.Core
import com.github.sandroln.core.DependencyContainer
import com.github.sandroln.core.NavigationCommunication
import com.github.sandroln.createboard.CreateBoardDependencyContainer
import com.github.sandroln.createticket.CreateTicketDependencyContainer
import com.github.sandroln.editticket.EditTicketDependencyContainer
import com.github.sandroln.login.LoginDependencyContainer
import com.github.sandroln.profile.ProfileDependencyContainer

interface MakeDependencies {

    fun dependencies(): DependencyContainer

    class Base(private val context: Context) : MakeDependencies {

        override fun dependencies(): DependencyContainer {
            val navigation = NavigationCommunication.Base()
            val featuresNavigation = FeaturesNavigation.Base(navigation)
            val core = Core.Base(MyUser.Base(featuresNavigation), context, navigation)
            val wrapper = BoardScopeModuleWrapper()
            val error = DependencyContainer.Error()
            val login = LoginDependencyContainer(core, featuresNavigation, error)
            val profile = ProfileDependencyContainer(core, login)
            val boards = BoardsDependencyContainer(wrapper, core, featuresNavigation, profile)
            val createBoard =
                CreateBoardDependencyContainer(wrapper, core, featuresNavigation, boards)
            val boardSettings = BoardSettingsDependencyContainer(wrapper, core, createBoard)
            val board = BoardDependencyContainer(wrapper, core, featuresNavigation, boardSettings)
            val createTicket = CreateTicketDependencyContainer(wrapper, core, board)
            val editTicket = EditTicketDependencyContainer(wrapper, core, createTicket)
            return BaseDependencyContainer(featuresNavigation, core, editTicket)
        }
    }
}