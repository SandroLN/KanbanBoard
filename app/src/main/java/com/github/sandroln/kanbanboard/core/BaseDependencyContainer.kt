package com.github.sandroln.kanbanboard.core

import androidx.lifecycle.ViewModel
import com.github.sandroln.core.Core
import com.github.sandroln.core.DependencyContainer
import com.github.sandroln.kanbanboard.main.MainModule
import com.github.sandroln.kanbanboard.main.MainViewModel

class BaseDependencyContainer(
    private val featuresNavigation: FeaturesNavigation,
    private val core: Core,
    private val dependencyContainer: DependencyContainer,
) : DependencyContainer {

    override fun module(className: Class<out ViewModel>) =
        if (className == MainViewModel::class.java)
            MainModule(core, featuresNavigation)
        else
            dependencyContainer.module(className)
}