package com.github.sandroln.kanbanboard.login

import com.github.sandroln.kanbanboard.core.Core
import com.github.sandroln.kanbanboard.core.Module
import com.github.sandroln.kanbanboard.login.data.LoginCloudDataSource
import com.github.sandroln.kanbanboard.login.data.LoginRepository
import com.github.sandroln.kanbanboard.login.presentation.LoginCommunication
import com.github.sandroln.kanbanboard.login.presentation.LoginViewModel
import com.github.sandroln.kanbanboard.service.Auth

class LoginModule(private val core: Core) : Module<LoginViewModel> {

    override fun viewModel() = LoginViewModel(
        LoginRepository.Base(
            Auth.Base(),
            core.provideMyUser(),
            LoginCloudDataSource.Base(core.provideMyUser(), core.service())
        ),
        core.provideDispatchersList(),
        core.manageResource(),
        LoginCommunication.Base(),
        core.navigation(),
    )
}