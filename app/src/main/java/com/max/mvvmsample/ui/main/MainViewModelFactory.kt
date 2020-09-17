package com.max.mvvmsample.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.max.mvvmsample.data.repositories.MainRepository
import com.max.mvvmsample.data.repositories.TimeRepository
import com.max.mvvmsample.data.repositories.WelcomeRepository

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(
    private val timeRepository: TimeRepository,
    private val welcomeRepository: WelcomeRepository,
    private val mainRepository: MainRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T = MainViewModel(timeRepository, welcomeRepository, mainRepository) as T
}