package com.github.sandroln.boards.myinvitations.data

import com.github.sandroln.chosenboard.OtherBoardCloud
import com.github.sandroln.cloudservice.Service

internal interface Invitations {

    interface Callback {

        fun provideInvitations(list: List<Pair<String, OtherBoardCloud>>)

        object Empty : Callback {

            override fun provideInvitations(list: List<Pair<String, OtherBoardCloud>>) = Unit
        }
    }

    interface CloudDataSource {

        fun init(callback: Callback)

        fun handle(userId: String)

        class Base(private val service: Service) : CloudDataSource {

            private var callback: Callback = Callback.Empty

            override fun init(callback: Callback) {
                this.callback = callback
            }

            private val listener = object : Service.Callback<OtherBoardCloud> {
                override fun provide(obj: List<Pair<String, OtherBoardCloud>>) =
                    callback.provideInvitations(obj)

                override fun error(message: String) = Unit//todo
            }

            override fun handle(userId: String) = service.getByQueryAsync(
                "boards-invitations",
                "memberId",
                userId,
                OtherBoardCloud::class.java,
                listener
            )
        }
    }
}