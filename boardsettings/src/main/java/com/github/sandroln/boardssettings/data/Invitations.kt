package com.github.sandroln.boardssettings.data

import com.github.sandroln.chosenboard.OtherBoardCloud
import com.github.sandroln.cloudservice.Service

internal interface Invitations {

    interface Callback {

        fun provideInvitations(invitedMemberIds: List<String>)

        object Empty : Callback {

            override fun provideInvitations(invitedMemberIds: List<String>) = Unit
        }
    }

    interface CloudDataSource {

        interface Init {

            fun init(callback: Callback)
        }

        interface Handle {

            fun handle(boardId: String)
        }

        interface Mutable : Init, Handle

        class Base(private val service: Service) : Mutable {

            private var callback: Callback = Callback.Empty

            override fun init(callback: Callback) {
                this.callback = callback
            }

            override fun handle(boardId: String) = service.getByQueryAsync(
                "boards-invitations",
                "boardId",
                boardId,
                OtherBoardCloud::class.java,
                object : Service.Callback<OtherBoardCloud> {
                    override fun provide(obj: List<Pair<String, OtherBoardCloud>>) {
                        val list = obj.map { it.second.memberId }
                        callback.provideInvitations(list)
                    }

                    override fun error(message: String) = Unit//todo
                }
            )
        }
    }
}