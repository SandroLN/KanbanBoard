package com.github.sandroln.login

import androidx.lifecycle.ViewModel
import com.github.sandroln.core.Core
import com.github.sandroln.core.DependencyContainer
import com.github.sandroln.core.Module
import com.github.sandroln.login.data.NavigateFromLogin
import com.github.sandroln.login.presentation.LoginViewModel

class LoginDependencyContainer(
    private val core: Core,
    private val navigateFromLogin: NavigateFromLogin,
    private val other: DependencyContainer
) : DependencyContainer {
    override fun module(className: Class<out ViewModel>): Module<out ViewModel> =
        if (className == LoginViewModel::class.java)
            LoginModule(core, navigateFromLogin)
        else
            other.module(className)
}