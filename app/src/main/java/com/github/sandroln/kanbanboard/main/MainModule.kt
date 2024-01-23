package com.github.sandroln.kanbanboard.main

import com.github.sandroln.cloudservice.NavigateToLoginScreen
import com.github.sandroln.core.Core
import com.github.sandroln.core.Module

class MainModule(
    private val core: Core,
    private val navigateToLoginScreen: NavigateToLoginScreen
) : Module<MainViewModel> {

    override fun viewModel(): MainViewModel = MainViewModel(navigateToLoginScreen, core.navigation())
}