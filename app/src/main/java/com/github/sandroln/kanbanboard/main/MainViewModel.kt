package com.github.sandroln.kanbanboard.main

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.github.sandroln.kanbanboard.core.Init
import com.github.sandroln.kanbanboard.login.presentation.LoginScreen

class MainViewModel(private val navigationCommunication: NavigationCommunication.Mutable) :
    ViewModel(), NavigationCommunication.Observe, Init {

    override fun observe(owner: LifecycleOwner, observer: Observer<Screen>) =
        navigationCommunication.observe(owner, observer)

    override fun init(firstRun: Boolean) {
        if (firstRun) navigationCommunication.map(LoginScreen)
    }
}