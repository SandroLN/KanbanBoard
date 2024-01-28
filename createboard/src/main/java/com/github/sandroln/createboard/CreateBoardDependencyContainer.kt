package com.github.sandroln.createboard

import androidx.lifecycle.ViewModel
import com.github.sandroln.chosenboard.BoardCore
import com.github.sandroln.core.DependencyContainer
import com.github.sandroln.createboard.presentation.CreateBoardViewModel

class CreateBoardDependencyContainer(
    private val core: BoardCore,
    private val navigation: CreateBoardNavigationList,
    private val other: DependencyContainer
) : DependencyContainer {

    override fun module(className: Class<out ViewModel>) =
        if (className == CreateBoardViewModel::class.java)
            CreateBoardModule(core, navigation)
        else
            other.module(className)
}