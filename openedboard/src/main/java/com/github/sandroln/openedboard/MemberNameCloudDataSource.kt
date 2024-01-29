package com.github.sandroln.openedboard

import com.github.sandroln.cloudservice.Service
import com.github.sandroln.common.UserProfileCloud
import com.github.sandroln.core.ProvideError

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