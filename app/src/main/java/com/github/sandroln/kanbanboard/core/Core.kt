package com.github.sandroln.kanbanboard.core

import android.content.Context
import com.github.sandroln.kanbanboard.board.BoardScopeModule
import com.github.sandroln.kanbanboard.board.ClearBoardScopeModule
import com.github.sandroln.kanbanboard.board.ProvideBoardScopeModule
import com.github.sandroln.kanbanboard.login.data.MyUser
import com.github.sandroln.kanbanboard.main.NavigationCommunication
import com.google.firebase.FirebaseApp
import com.google.gson.Gson

class Core(context: Context) : ProvideNavigation, ProvideStorage, ProvideManageResource,
    ProvideDispatchersList, ProvideDatabase, ProvideSerialization, ProvideBoardScopeModule,
    ClearBoardScopeModule, ProvideMyUser {

    init {
        FirebaseApp.initializeApp(context)
    }

    private val serialization: Serialization.Mutable = Serialization.Base(Gson())
    private val provideDatabase = ProvideDatabase.Base()
    private val manageResource = ManageResource.Base(context)
    private val navigation = NavigationCommunication.Base()
    private val storage = SimpleStorage.Base(
        context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE)
    ).let { simpleStorage ->
        Storage.Base(simpleStorage, ObjectStorage.Base(simpleStorage, Gson()))
    }

    private val dispatchersList = DispatchersList.Base()

    override fun provideDispatchersList() = dispatchersList

    override fun navigation(): NavigationCommunication.Mutable {
        return navigation
    }

    override fun storage() = storage

    companion object {
        private const val STORAGE_NAME = "KANBAN BOARDS APP DATA"
    }

    override fun manageResource() = manageResource

    override fun database() = provideDatabase.database()

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

    private val myUser = MyUser.Base(navigation)

    override fun provideMyUser() = myUser
}

interface ProvideNavigation {

    fun navigation(): NavigationCommunication.Mutable
}

interface ProvideStorage {

    fun storage(): Storage
}

interface ProvideManageResource {

    fun manageResource(): ManageResource
}

interface ProvideDispatchersList {
    fun provideDispatchersList(): DispatchersList
}

interface ProvideSerialization {
    fun serialization(): Serialization.Mutable
}

interface ProvideMyUser {
    fun provideMyUser(): MyUser
}