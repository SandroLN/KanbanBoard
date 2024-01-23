package com.github.sandroln.core

import com.github.sandroln.cloudservice.MyUser
import com.github.sandroln.cloudservice.Service

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
    ProvideDispatchersList, ProvideSerialization, ProvideMyUser, ProvideService