package com.github.sandroln.kanbanboard.board.settings.data

import com.github.sandroln.chosenboard.BoardCache
import com.github.sandroln.chosenboard.ChosenBoardCache
import com.github.sandroln.chosenboard.OtherBoardCloud
import com.github.sandroln.cloudservice.MyUser
import com.github.sandroln.cloudservice.Service
import com.github.sandroln.common.UserProfileCloud
import com.github.sandroln.openedboard.BoardUser

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
            chosenBoardCache.read().map(OtherBoardCloudMapper(user))
        )
    }
}

class OtherBoardCloudMapper(
    private val user: BoardUser
) : BoardCache.Mapper<OtherBoardCloud> {

    override fun map(
        id: String,
        name: String,
        isMyBoard: Boolean,
        ownerId: String
    ): OtherBoardCloud = OtherBoardCloud(user.id(), id)
}