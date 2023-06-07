package com.github.sandroln.kanbanboard.profile

import com.github.sandroln.kanbanboard.core.Core
import com.github.sandroln.kanbanboard.core.Module
import com.github.sandroln.kanbanboard.profile.presentation.ProfileCommunication
import com.github.sandroln.kanbanboard.profile.presentation.ProfileViewModel

class ProfileModule(private val core: Core) : Module<ProfileViewModel> {
    override fun viewModel(): ProfileViewModel {
        return ProfileViewModel(ProfileCommunication.Base(), core.navigation())
    }
}