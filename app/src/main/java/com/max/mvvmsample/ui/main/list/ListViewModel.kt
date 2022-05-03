@file:Suppress("unused")

package com.max.mvvmsample.ui.main.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.max.mvvmsample.data.entities.ListEntity
import com.max.mvvmsample.data.repositories.ListRepository
import com.max.mvvmsample.utils.Coroutines
import com.xwray.groupie.GroupieAdapter

class ListViewModel(
    private val listRepository: ListRepository
) : ViewModel() {

    val entity = ListEntity()
    val throwMessage = MutableLiveData("")

    val adapter = GroupieAdapter()

    //TODO Get List Data
    fun refreshList() {

        entity.hasData.value = false

        getList()
    }

    private fun getList() = Coroutines.io {

        runCatching {
            listRepository.post()
        }.onSuccess { dataList ->

            Coroutines.main {

                entity.hasData.value = true

                with(adapter) {
                    clear()
                    addAll(dataList.map { data -> ListItem(data) })
                }
            }

        }.onFailure {

            it.printStackTrace()

            Coroutines.main {

                entity.hasData.value = false

                adapter.clear()

                throwMessage.value = it.message
            }
        }
    }
}