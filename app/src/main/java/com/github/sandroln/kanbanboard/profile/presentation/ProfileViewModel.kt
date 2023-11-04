package com.github.sandroln.kanbanboard.profile.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.github.sandroln.kanbanboard.core.Communication
import com.github.sandroln.kanbanboard.core.GoBack
import com.github.sandroln.kanbanboard.core.Init
import com.github.sandroln.kanbanboard.login.data.MyUser
import com.github.sandroln.kanbanboard.main.NavigationCommunication
import com.github.sandroln.kanbanboard.main.Screen

class ProfileViewModel(
    private val myUser: MyUser,
    private val communication: ProfileCommunication,
    private val navigationCommunication: NavigationCommunication.Update
) : ViewModel(), Init, Communication.Observe<ProfileUiState>, GoBack {

    override fun observe(owner: LifecycleOwner, observer: Observer<ProfileUiState>) =
        communication.observe(owner, observer)

    fun signOut() = myUser.signOut()

    override fun init(firstRun: Boolean) = communication.map(myUser.profile())

    override fun goBack() = navigationCommunication.map(Screen.Pop)
}