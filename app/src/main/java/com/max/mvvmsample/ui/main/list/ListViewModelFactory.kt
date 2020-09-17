package com.max.mvvmsample.ui.main.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.max.mvvmsample.data.repositories.ListRepository

@Suppress("UNCHECKED_CAST")
class ListViewModelFactory(
    private val listRepository: ListRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T = ListViewModel(listRepository) as T
}