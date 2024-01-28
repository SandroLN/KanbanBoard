package com.github.sandroln.login

import androidx.lifecycle.ViewModel
import com.github.sandroln.common.NavigateToBoards
import com.github.sandroln.core.Core
import com.github.sandroln.core.DependencyContainer
import com.github.sandroln.core.Module
import com.github.sandroln.login.presentation.LoginViewModel

class LoginDependencyContainer(
    private val core: Core,
    private val navigateToBoards: NavigateToBoards,
    private val other: DependencyContainer
) : DependencyContainer {

    override fun module(className: Class<out ViewModel>): Module<out ViewModel> =
        if (className == LoginViewModel::class.java)
            LoginModule(core, navigateToBoards)
        else
            other.module(className)
}