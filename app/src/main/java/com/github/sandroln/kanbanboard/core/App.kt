package com.github.sandroln.kanbanboard.core

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.github.sandroln.boards.BoardsDependencyContainer
import com.github.sandroln.core.DependencyContainer
import com.github.sandroln.core.NavigationCommunication
import com.github.sandroln.core.ProvideViewModel
import com.github.sandroln.login.LoginDependencyContainer
import com.github.sandroln.profile.ProfileDependencyContainer

class App : Application(), ProvideViewModel {

    private lateinit var viewModelsFactory: ViewModelsFactory

    override fun onCreate() {
        super.onCreate()
        val navigation = NavigationCommunication.Base()
        val featuresNavigation = FeaturesNavigation.Base(navigation)
        val core = CoreImpl(featuresNavigation, this, navigation)
        val login = LoginDependencyContainer(core, featuresNavigation, DependencyContainer.Error())
        val profile = ProfileDependencyContainer(core, login)
        val boards = BoardsDependencyContainer(core, featuresNavigation, profile)
        viewModelsFactory =
            ViewModelsFactory(BaseDependencyContainer(featuresNavigation, core, boards))
    }

    override fun <T : ViewModel> viewModel(owner: ViewModelStoreOwner, className: Class<T>): T =
        ViewModelProvider(owner, viewModelsFactory)[className]
}