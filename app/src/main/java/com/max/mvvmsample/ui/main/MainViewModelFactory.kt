package com.max.mvvmsample.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.max.mvvmsample.data.repositories.MainRepository

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(
    private val mainRepository: MainRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = MainViewModel(mainRepository) as T
}