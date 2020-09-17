package com.max.mvvmsample.ui.main.list

import androidx.lifecycle.ViewModel
import com.max.mvvmsample.data.entities.ListEntity
import com.max.mvvmsample.data.repositories.ListRepository
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ListViewModel(
    private val listRepository: ListRepository
) : ViewModel() {

    val entity = ListEntity()

    val adapter = GroupAdapter<ViewHolder>()

    suspend fun post() = withContext(Dispatchers.IO) { listRepository.post() }
}