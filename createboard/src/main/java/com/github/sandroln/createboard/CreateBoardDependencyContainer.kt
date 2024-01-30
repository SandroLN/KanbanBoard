package com.github.sandroln.createboard

import androidx.lifecycle.ViewModel
import com.github.sandroln.chosenboard.ClearBoardScopeModule
import com.github.sandroln.core.Core
import com.github.sandroln.core.DependencyContainer
import com.github.sandroln.createboard.presentation.CreateBoardViewModel

class CreateBoardDependencyContainer(
    private val clearBoardScopeModule: ClearBoardScopeModule,
    private val core: Core,
    private val navigation: CreateBoardNavigationList,
    private val other: DependencyContainer
) : DependencyContainer {

    override fun module(className: Class<out ViewModel>) =
        if (className == CreateBoardViewModel::class.java)
            CreateBoardModule(clearBoardScopeModule, core, navigation)
        else
            other.module(className)
}