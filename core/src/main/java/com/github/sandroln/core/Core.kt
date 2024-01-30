package com.github.sandroln.core

import android.content.Context
import com.github.sandroln.cloudservice.MyUser
import com.github.sandroln.cloudservice.Service
import com.google.gson.Gson

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

interface ProvideService {
    fun service(): Service
}

interface Core : ProvideNavigation, ProvideStorage, ProvideManageResource,
    ProvideDispatchersList, ProvideSerialization, ProvideMyUser, ProvideService {

    class Base(
        private val myUser: MyUser,
        context: Context,
        private val navigation: NavigationCommunication.Mutable
    ) : Core {

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

        override fun provideMyUser() = myUser

        override fun service() = service
    }
}