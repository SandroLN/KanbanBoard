package com.github.sandroln.kanbanboard.core

import android.content.Context
import com.github.sandroln.kanbanboard.main.NavigationCommunication
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference

class Core(context: Context) : ProvideNavigation, ProvideStorage, ProvideManageResource,
    ProvideDispatcherList, ProvideDatabase {

    init {
        FirebaseApp.initializeApp(context)
    }

    private val provideDatabase = ProvideDatabase.Base()
    private val manageResource = ManageResource.Base(context)
    private val navigation = NavigationCommunication.Base()
    private val storage =
        Storage.Base(context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE))
    private val dispatcherList = DispatchersList.Base()

    override fun provideDispatchersList() = dispatcherList

    override fun navigation(): NavigationCommunication.Mutable {
        return navigation
    }

    override fun storage() = storage

    companion object {
        private const val STORAGE_NAME = "KANBAN BOARD APP DATA"
    }

    override fun manageResource() = manageResource


    override fun database(): DatabaseReference = provideDatabase.database()

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

interface ProvideDispatcherList {

    fun provideDispatchersList(): DispatchersList
}