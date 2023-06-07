package com.github.sandroln.kanbanboard.main

import com.github.sandroln.kanbanboard.core.Core
import com.github.sandroln.kanbanboard.core.Module

class MainModule(private val core: Core) : Module<MainViewModel> {

    override fun viewModel(): MainViewModel = MainViewModel(core.navigation())
}