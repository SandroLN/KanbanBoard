package com.github.sandroln.cloudservice

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

internal interface ProvideDatabase {

    fun database(): DatabaseReference

    class Base(context: Context) : ProvideDatabase {

        init {
            FirebaseApp.initializeApp(context)
            Firebase.database(DATABASE_URL).setPersistenceEnabled(false)
        }

        override fun database(): DatabaseReference {
            return Firebase.database(DATABASE_URL).reference.root
        }

        companion object {
            private const val DATABASE_URL =
                "https://kanban-board-98201-default-rtdb.europe-west1.firebasedatabase.app/"
        }
    }
}