package com.max.mvvmsample.ui.main.list

import android.view.View
import com.max.mvvmsample.R
import com.max.mvvmsample.data.entities.item.ListItemEntity
import com.max.mvvmsample.data.network.response.ResponseData
import com.max.mvvmsample.databinding.ItemListBinding
import com.xwray.groupie.viewbinding.BindableItem

class ListItem(
    private val data: ResponseData.Data,
    private val itemClickListener: ItemClickListener? = null
) : BindableItem<ItemListBinding>() {

    override fun getLayout() = R.layout.item_list

    override fun initializeViewBinding(view: View) = ItemListBinding.bind(view)

    override fun bind(binding: ItemListBinding, position: Int) {

        with(binding) {
            click = View.OnClickListener { itemClickListener?.onClick(data) }
            entity = data.toListItemEntity()
        }
    }

    private fun ResponseData.Data.toListItemEntity() = ListItemEntity(
        data = data
    )

    fun interface ItemClickListener {
        fun onClick(data: ResponseData.Data)
    }
}