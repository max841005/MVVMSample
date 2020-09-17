package com.max.mvvmsample.ui.main.list

import android.view.View
import com.max.mvvmsample.R
import com.max.mvvmsample.data.network.response.ResponseData
import com.max.mvvmsample.databinding.ItemListBinding
import com.xwray.groupie.databinding.BindableItem

class ListItem(
    private val data: ResponseData.Data
) : BindableItem<ItemListBinding>() {

    private var itemClickListener: ItemClickListener? = null

    override fun getLayout() = R.layout.item_list

    override fun bind(binding: ItemListBinding, position: Int) {

        binding.data = data

        binding.click = View.OnClickListener { itemClickListener?.onClick(data) }
    }

    fun setItemClickListener(itemClickListener: ItemClickListener) { this.itemClickListener = itemClickListener }

    interface ItemClickListener {
        fun onClick(data: ResponseData.Data)
    }
}