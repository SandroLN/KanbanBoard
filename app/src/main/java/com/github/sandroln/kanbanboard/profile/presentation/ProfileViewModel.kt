package com.github.sandroln.kanbanboard.profile.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.github.sandroln.kanbanboard.core.Communication
import com.github.sandroln.kanbanboard.core.GoBack
import com.github.sandroln.kanbanboard.core.Init
import com.github.sandroln.kanbanboard.login.presentation.LoginScreen
import com.github.sandroln.kanbanboard.main.NavigationCommunication
import com.github.sandroln.kanbanboard.main.Screen
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileViewModel(
    private val communication: ProfileCommunication,
    private val navigationCommunication: NavigationCommunication.Update
) : ViewModel(), Init, Communication.Observe<ProfileUiState>, GoBack {

    override fun observe(owner: LifecycleOwner, observer: Observer<ProfileUiState>) =
        communication.observe(owner, observer)

    fun signOut() {
        Firebase.auth.signOut()
        navigationCommunication.map(LoginScreen)
    }

    override fun init(firstRun: Boolean) {
        if (Firebase.auth.currentUser == null)
            navigationCommunication.map(LoginScreen)
        else {
            val currentUser = Firebase.auth.currentUser!!
            communication.map(
                ProfileUiState.Base(
                    currentUser.email!!,
                    currentUser.displayName ?: ""
                )
            )
        }
    }

    override fun goBack() = navigationCommunication.map(Screen.Pop)
}