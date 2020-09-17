package com.max.mvvmsample.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.max.mvvmsample.data.repositories.HomeRepository
import com.max.mvvmsample.data.repositories.TimeRepository

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory(
    private val timeRepository: TimeRepository,
    private val homeRepository: HomeRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T = HomeViewModel(timeRepository, homeRepository) as T
}