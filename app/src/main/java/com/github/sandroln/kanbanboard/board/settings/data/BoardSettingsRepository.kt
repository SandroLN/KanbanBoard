package com.github.sandroln.kanbanboard.board.settings.data

import com.github.sandroln.cloudservice.MyUser
import com.github.sandroln.cloudservice.Service
import com.github.sandroln.kanbanboard.board.main.data.BoardUser
import com.github.sandroln.kanbanboard.boards.data.ChosenBoardCache
import com.github.sandroln.kanbanboard.login.data.UserProfileCloud

interface BoardSettingsRepository {

    suspend fun findUsers(userEmail: String): List<Pair<String, UserProfileCloud>>

    fun inviteUserToBoard(user: BoardUser)

    class Base(
        private val myUser: MyUser,
        private val chosenBoardCache: ChosenBoardCache.Read,
        private val service: Service
    ) : BoardSettingsRepository {

        override suspend fun findUsers(userEmail: String): List<Pair<String, UserProfileCloud>> {
            val list = service.getByQueryStartWith(
                "users",
                "mail",
                userEmail,
                UserProfileCloud::class.java
            )

            return list.filter {
                it.second.mail.startsWith(userEmail, true) && it.first != myUser.id()
            }
        }

        override fun inviteUserToBoard(user: BoardUser) = service.postFirstLevelAsync(
            "boards-invitations",
            chosenBoardCache.read().invite(user)
        )
    }
}