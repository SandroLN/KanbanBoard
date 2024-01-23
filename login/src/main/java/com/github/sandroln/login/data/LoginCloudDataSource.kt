package com.github.sandroln.login.data

import com.github.sandroln.cloudservice.MyUser
import com.github.sandroln.cloudservice.Service
import com.github.sandroln.common.UserProfileCloud

internal interface LoginCloudDataSource {

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