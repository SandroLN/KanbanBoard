package com.github.sandroln.login.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.github.sandroln.core.BaseViewModel
import com.github.sandroln.core.Communication
import com.github.sandroln.core.DispatchersList
import com.github.sandroln.core.Init
import com.github.sandroln.core.ManageResource
import com.github.sandroln.login.data.LoginRepository
import com.github.sandroln.login.data.NavigateFromLogin

internal class LoginViewModel(
    private val repository: LoginRepository,
    dispatchersList: DispatchersList,
    private val manageResource: ManageResource,
    private val communication: LoginCommunication,
    private val navigation: NavigateFromLogin
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