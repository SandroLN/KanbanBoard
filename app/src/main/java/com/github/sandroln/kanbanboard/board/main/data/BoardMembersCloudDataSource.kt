package com.github.sandroln.kanbanboard.board.main.data

import com.github.sandroln.cloudservice.MyUser
import com.github.sandroln.cloudservice.Service
import com.github.sandroln.kanbanboard.boards.data.OtherBoardCloud

interface BoardMembers {

    interface Callback {

        fun provideAllMembersIds(members: Set<String>)

        object Empty : Callback {
            override fun provideAllMembersIds(members: Set<String>) = Unit
        }
    }

    interface CloudDataSource {

        fun init(callback: Callback)

        fun provideAllMembers(boardId: String, isMyBoard: Boolean, ownerId: String)

        class Base(
            private val myUser: MyUser,
            private val provideError: com.github.sandroln.core.ProvideError,
            private val service: Service,
        ) : CloudDataSource {
            private var callback: Callback = Callback.Empty

            override fun init(callback: Callback) {
                this.callback = callback
            }

            override fun provideAllMembers(boardId: String, isMyBoard: Boolean, ownerId: String) {
                val boardMembersIds = mutableSetOf<String>()
                boardMembersIds.add(if (isMyBoard) myUser.id() else ownerId)

                val listener = object : Service.Callback<OtherBoardCloud> {
                    override fun provide(obj: List<Pair<String, OtherBoardCloud>>) {
                        val memberIds = obj.map { it.second.memberId }
                        boardMembersIds.addAll(memberIds)
                        callback.provideAllMembersIds(boardMembersIds)
                    }

                    override fun error(message: String) = provideError.error(message)
                }

                service.getByQueryAsync(
                    "boards-members",
                    "boardId",
                    boardId,
                    OtherBoardCloud::class.java,
                    listener
                )
            }
        }
    }
}