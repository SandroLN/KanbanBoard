package com.github.sandroln.cloudservice

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface Service {

    interface Callback<T : Any> {

        fun provide(obj: List<Pair<String, T>>)

        fun error(message: String)
    }

    fun <T : Any?> referenceWrapper(
        path: String,
        child: String,
        clasz: Class<T>
    ): ReferenceWrapper<T>

    fun remove(path: String, child: String)

    fun updateField(path: String, child: String, fieldId: String, fieldValue: Any)

    fun <T : Any> getByQueryAsync(
        path: String,
        queryKey: String,
        queryValue: String,
        clasz: Class<T>,
        callback: Callback<T>
    )

    suspend fun <T : Any> getByQuery(
        path: String,
        queryKey: String,
        queryValue: String,
        clasz: Class<T>
    ): List<Pair<String, T>>

    suspend fun <T : Any> getByQueryStartWith(
        path: String,
        queryKey: String,
        queryValue: String,
        clasz: Class<T>,
        limit: Int = 100
    ): List<Pair<String, T>>

    suspend fun <T : Any> getByQuery(
        path: String,
        queryValue: String,
        clasz: Class<T>
    ): List<Pair<String, T>>

    fun <T : Any> getByQueryAsyncOneTime(
        path: String,
        queryValue: String,
        clasz: Class<T>,
        callback: Callback<T>
    )

    fun postFirstLevelAsync(path: String, obj: Any)

    suspend fun postFirstLevel(path: String, obj: Any): String

    suspend fun createWithId(path: String, id: String, value: Any)

    class Base(context: Context) : Service {

        private val database = ProvideDatabase.Base(context).database()

        override fun <T> referenceWrapper(
            path: String,
            child: String,
            clasz: Class<T>
        ): ReferenceWrapper<T> {
            return ReferenceWrapper.Base(database, path, child, clasz)
        }

        override fun remove(path: String, child: String) {
            database.child(path).child(child).removeValue()
        }

        override fun updateField(path: String, child: String, fieldId: String, fieldValue: Any) {
            database
                .child(path)
                .child(child)
                .child(fieldId)
                .setValue(fieldValue)
        }

        override fun <T : Any> getByQueryAsync(
            path: String,
            queryKey: String,
            queryValue: String,
            clasz: Class<T>,
            callback: Callback<T>
        ) {
            database
                .child(path)
                .orderByChild(queryKey)
                .equalTo(queryValue)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val data = snapshot.children.mapNotNull {
                            Pair(it.key!!, it.getValue(clasz)!!)
                        }
                        callback.provide(data)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        callback.error(error.message)
                    }
                })
        }

        override suspend fun <T : Any> getByQuery(
            path: String,
            queryKey: String,
            queryValue: String,
            clasz: Class<T>
        ): List<Pair<String, T>> {
            val query = database
                .child(path)
                .orderByChild(queryKey)
                .equalTo(queryValue)
            return handleQuery(query, clasz)
        }

        override suspend fun <T : Any> getByQuery(
            path: String,
            queryValue: String,
            clasz: Class<T>
        ): List<Pair<String, T>> {
            val query = database
                .child(path)
                .orderByKey()
                .equalTo(queryValue)
            return handleQuery(query, clasz)
        }

        override fun <T : Any> getByQueryAsyncOneTime(
            path: String,
            queryValue: String,
            clasz: Class<T>,
            callback: Callback<T>
        ) {
            database
                .child(path)
                .orderByKey()
                .equalTo(queryValue)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val list = snapshot.children.mapNotNull {
                            Pair(
                                it.key!!,
                                it.getValue(clasz)!!
                            )
                        }
                        callback.provide(list)
                    }

                    override fun onCancelled(error: DatabaseError) = callback.error(error.message)
                })
        }

        override suspend fun <T : Any> getByQueryStartWith(
            path: String,
            queryKey: String,
            queryValue: String,
            clasz: Class<T>,
            limit: Int
        ): List<Pair<String, T>> {
            val query = database
                .child(path)
                .orderByChild(queryKey)
                .startAt(queryValue)
                .limitToFirst(limit)
            return handleQuery(query, clasz)
        }

        private suspend fun <T : Any> handleQuery(
            query: Query,
            clasz: Class<T>
        ) = suspendCoroutine { cont ->
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.children.mapNotNull {
                        Pair(
                            it.key!!,
                            it.getValue(clasz)!!
                        )
                    }
                    cont.resume(data)
                }

                override fun onCancelled(error: DatabaseError) =
                    cont.resumeWithException(IllegalStateException(error.message))
            })
        }

        override fun postFirstLevelAsync(path: String, obj: Any) {
            database.child(path).push().setValue(obj)
        }

        override suspend fun postFirstLevel(path: String, obj: Any): String {
            val reference = database.child(path).push()
            val task = reference.setValue(obj)
            handleResult(task)
            return reference.key!!
        }

        override suspend fun createWithId(path: String, id: String, value: Any) {
            val result = database
                .child(path)
                .child(id)
                .setValue(value)
            handleResult(result)
        }

        private suspend fun handleResult(value: Task<Void>): Unit =
            suspendCoroutine { continuation ->
                value.addOnSuccessListener {
                    continuation.resume(Unit)
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
            }
    }
}

interface ReferenceWrapper<T : Any?> {

    interface Callback<T : Any?> {
        fun provide(obj: T)
        fun error(message: String)
    }

    fun startListen(callback: Callback<T?>)

    fun change(key: String, newValue: Any)

    fun delete()

    class Base<T : Any?>(
        database: DatabaseReference,
        path: String,
        child: String,
        private val clasz: Class<T>
    ) : ReferenceWrapper<T> {

        private val reference = database.child(path).child(child)

        override fun startListen(callback: Callback<T?>) {
            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val obj = snapshot.getValue(clasz)
                    callback.provide(obj)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback.error(error.message)
                }
            })
        }

        override fun change(key: String, newValue: Any) {
            reference.child(key).setValue(newValue)
        }

        override fun delete() {
            reference.removeValue()
        }
    }
}