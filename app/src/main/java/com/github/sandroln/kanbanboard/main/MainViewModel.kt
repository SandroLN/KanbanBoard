package com.github.sandroln.kanbanboard.main

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.github.sandroln.cloudservice.NavigateToLoginScreen
import com.github.sandroln.core.ConnectedCommunication
import com.github.sandroln.core.Init
import com.github.sandroln.core.NavigationCommunication
import com.github.sandroln.core.Screen

class MainViewModel(
    private val connectedCommunication: ConnectedCommunication.Observe,
    private val navigateToLoginScreen: NavigateToLoginScreen,
    private val navigationCommunication: NavigationCommunication.Observe
) : ViewModel(), NavigationCommunication.Observe, Init {

    fun observeConnection(owner: LifecycleOwner, observer: Observer<Boolean>) =
        connectedCommunication.observe(owner, observer)

    override fun observe(owner: LifecycleOwner, observer: Observer<Screen>) =
        navigationCommunication.observe(owner, observer)

    override fun init(firstRun: Boolean) {
        if (firstRun) navigateToLoginScreen.navigateToLoginScreen()
    }
}