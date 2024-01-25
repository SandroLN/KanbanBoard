package com.github.sandroln.profile.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.github.sandroln.cloudservice.MyUser
import com.github.sandroln.core.Communication
import com.github.sandroln.core.GoBack
import com.github.sandroln.core.Init
import com.github.sandroln.core.NavigationCommunication
import com.github.sandroln.core.Screen

internal class ProfileViewModel(
    private val myUser: MyUser,
    private val communication: ProfileCommunication,
    private val navigationCommunication: NavigationCommunication.Update
) : ViewModel(), Init, Communication.Observe<ProfileUiState>,
    GoBack {

    override fun observe(owner: LifecycleOwner, observer: Observer<ProfileUiState>) =
        communication.observe(owner, observer)

    fun signOut() = myUser.signOut()

    override fun init(firstRun: Boolean) {
        val source = myUser.profile()
        val state = if (source.first.isEmpty() && source.second.isEmpty())
            ProfileUiState.Empty
        else
            ProfileUiState.Base(source.first, source.second)
        communication.map(state)
    }

    override fun goBack() = navigationCommunication.map(Screen.Pop)
}