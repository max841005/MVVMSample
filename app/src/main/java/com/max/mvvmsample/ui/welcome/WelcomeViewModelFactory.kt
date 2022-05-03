package com.max.mvvmsample.ui.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.max.mvvmsample.data.repositories.WelcomeRepository

@Suppress("UNCHECKED_CAST")
class WelcomeViewModelFactory(
    private val welcomeRepository: WelcomeRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T = WelcomeViewModel(welcomeRepository) as T
}