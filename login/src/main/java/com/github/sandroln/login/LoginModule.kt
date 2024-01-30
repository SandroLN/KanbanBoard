package com.github.sandroln.login

import com.github.sandroln.cloudservice.Auth
import com.github.sandroln.common.NavigateToBoards
import com.github.sandroln.core.Core
import com.github.sandroln.core.Module
import com.github.sandroln.login.data.LoginCloudDataSource
import com.github.sandroln.login.data.LoginRepository
import com.github.sandroln.login.presentation.LoginCommunication
import com.github.sandroln.login.presentation.LoginViewModel

internal class LoginModule(
    private val core: Core,
    private val navigateToBoards: NavigateToBoards
) : Module<LoginViewModel> {

    override fun viewModel() = LoginViewModel(
        LoginRepository.Base(
            Auth.Base(),
            core.provideMyUser(),
            LoginCloudDataSource.Base(core.provideMyUser(), core.service())
        ),
        core.provideDispatchersList(),
        core.manageResource(),
        LoginCommunication.Base(),
        navigateToBoards
    )
}