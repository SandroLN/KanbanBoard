package com.github.sandroln.kanbanboard.login.data

import com.github.sandroln.kanbanboard.service.MyUser
import com.github.sandroln.kanbanboard.service.Service

interface LoginCloudDataSource {

    suspend fun login()

    class Base(
        private val myUser: MyUser,
        private val service: Service
    ) : LoginCloudDataSource {

        override suspend fun login() {
            val id = myUser.id()
            val userProfile = myUser.userProfileCloud()
            service.createWithId("users", id, userProfile)
        }
    }
}

data class UserProfileCloud(
    val mail: String = "",
    val name: String = ""
)