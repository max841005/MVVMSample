package com.max.mvvmsample.ui.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.max.mvvmsample.data.repositories.TimeRepository
import com.max.mvvmsample.data.repositories.WelcomeRepository

@Suppress("UNCHECKED_CAST")
class WelcomeViewModelFactory(
    private val timeRepository: TimeRepository,
    private val welcomeRepository: WelcomeRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T = WelcomeViewModel(timeRepository, welcomeRepository) as T
}