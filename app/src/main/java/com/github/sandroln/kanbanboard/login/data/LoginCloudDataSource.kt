package com.github.sandroln.kanbanboard.login.data

import com.github.sandroln.cloudservice.MyUser
import com.github.sandroln.cloudservice.Service

interface LoginCloudDataSource {

    suspend fun login()

    class Base(
        private val myUser: MyUser,
        private val service: Service
    ) : LoginCloudDataSource {

        override suspend fun login() {
            val id = myUser.id()
            val (mail, name) = myUser.userProfileCloud()
            val userProfile = UserProfileCloud(mail, name)
            service.createWithId("users", id, userProfile)
        }
    }
}

data class UserProfileCloud(
    val mail: String = "",
    val name: String = ""
)