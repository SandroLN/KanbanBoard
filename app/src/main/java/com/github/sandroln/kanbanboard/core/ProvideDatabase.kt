package com.github.sandroln.kanbanboard.core

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

interface ProvideDatabase {

    fun database(): DatabaseReference

    class Base : ProvideDatabase {

        init {
            Firebase.database(DATABASE_URL).setPersistenceEnabled(true)
        }

        override fun database(): DatabaseReference {
            return Firebase.database(DATABASE_URL).reference.root
        }

        private companion object {
            const val DATABASE_URL =
                "https://kanban-board-98201-default-rtdb.europe-west1.firebasedatabase.app/"
        }
    }
}