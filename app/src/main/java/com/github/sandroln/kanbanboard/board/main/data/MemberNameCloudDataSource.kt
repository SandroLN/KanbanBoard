package com.github.sandroln.kanbanboard.board.main.data

import com.github.sandroln.kanbanboard.core.ProvideError
import com.github.sandroln.kanbanboard.login.data.UserProfileCloud
import com.github.sandroln.kanbanboard.service.Service

interface MemberName {

    interface Callback {

        fun provideMember(user: UserProfileCloud, userId: String)

        object Empty : Callback {
            override fun provideMember(user: UserProfileCloud, userId: String) = Unit
        }
    }

    interface CloudDataSource {
        fun init(callback: Callback)

        fun handle(memberId: String)

        class Base(
            private val provideError: ProvideError,
            private val service: Service,
        ) : CloudDataSource {

            private var callback: Callback = Callback.Empty

            override fun init(callback: Callback) {
                this.callback = callback
            }

            override fun handle(memberId: String) = service.getByQueryAsyncOneTime(
                "users",
                memberId,
                UserProfileCloud::class.java,
                object : Service.Callback<UserProfileCloud> {
                    override fun provide(obj: List<Pair<String, UserProfileCloud>>) =
                        obj[0].let { (id, user) -> callback.provideMember(user, id) }

                    override fun error(message: String) = provideError.error(message)
                }
            )
        }
    }
}