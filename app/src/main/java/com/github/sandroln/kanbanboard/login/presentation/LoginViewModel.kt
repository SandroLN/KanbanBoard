package com.github.sandroln.kanbanboard.login.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.github.sandroln.kanbanboard.core.BaseViewModel
import com.github.sandroln.kanbanboard.core.Communication
import com.github.sandroln.kanbanboard.core.DispatchersList
import com.github.sandroln.kanbanboard.core.Init
import com.github.sandroln.kanbanboard.core.ManageResource
import com.github.sandroln.kanbanboard.login.data.LoginRepository
import com.github.sandroln.kanbanboard.main.NavigationCommunication

class LoginViewModel(
    private val repository: LoginRepository,
    dispatchersList: DispatchersList,
    private val manageResource: ManageResource,
    private val communication: LoginCommunication,
    private val navigation: NavigationCommunication.Update
) : BaseViewModel(dispatchersList), Init, Communication.Observe<LoginUiState> {

    override fun observe(owner: LifecycleOwner, observer: Observer<LoginUiState>) =
        communication.observe(owner, observer)

    override fun init(firstRun: Boolean) {
        if (firstRun) {
            if (repository.userNotLoggedIn())
                communication.map(LoginUiState.Initial)
            else
                login()
        }
    }

    fun handleResult(authResult: AuthResultWrapper) = handle({
        repository.handleResult(authResult)
    }) {
        it.map(communication, navigation)
    }

    fun login() = communication.map(LoginUiState.Auth(manageResource))
}