package com.github.sandroln.kanbanboard.core

import android.content.Context
import com.github.sandroln.chosenboard.BoardCore
import com.github.sandroln.cloudservice.MyUser
import com.github.sandroln.cloudservice.Service
import com.github.sandroln.core.DispatchersList
import com.github.sandroln.core.ManageResource
import com.github.sandroln.core.NavigationCommunication
import com.github.sandroln.core.ObjectStorage
import com.github.sandroln.core.Serialization
import com.github.sandroln.core.SimpleStorage
import com.github.sandroln.core.Storage
import com.github.sandroln.openedboard.BoardScopeModule
import com.github.sandroln.openedboard.OpenedBoardCore
import com.google.gson.Gson

class CoreImpl(
    featuresNavigation: FeaturesNavigation,
    context: Context,
    private val navigation: NavigationCommunication.Mutable
) : BoardCore, OpenedBoardCore {

    private val myUser = MyUser.Base(featuresNavigation)
    private val service: Service = Service.Base(context)
    private val gson = Gson()
    private val serialization: Serialization.Mutable = Serialization.Base(gson)
    private val manageResource = ManageResource.Base(context)
    private val dispatchersList = DispatchersList.Base()
    private val storage = SimpleStorage.Base(
        context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE)
    ).let { simpleStorage ->
        Storage.Base(simpleStorage, ObjectStorage.Base(simpleStorage, gson))
    }

    override fun provideDispatchersList() = dispatchersList

    override fun navigation(): NavigationCommunication.Mutable = navigation

    override fun storage() = storage

    companion object {
        private const val STORAGE_NAME = "KANBAN BOARDS APP DATA"
    }

    override fun manageResource() = manageResource

    override fun serialization() = serialization

    private var boardScopeModule: BoardScopeModule? = null

    override fun boardScopeModule(): BoardScopeModule {
        if (boardScopeModule == null)
            boardScopeModule = BoardScopeModule.Base()
        return boardScopeModule!!
    }

    override fun clearBoardScopeModule() {
        boardScopeModule = null
    }

    override fun provideMyUser() = myUser

    override fun service() = service
}