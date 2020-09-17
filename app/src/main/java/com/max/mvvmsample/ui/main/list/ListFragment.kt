package com.max.mvvmsample.ui.main.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.max.mvvmsample.R
import com.max.mvvmsample.data.network.response.ResponseData
import com.max.mvvmsample.databinding.FragmentListBinding
import com.max.mvvmsample.ui.main.MainViewModel
import com.max.mvvmsample.view.RecyclerDivider
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class ListFragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    private val factory: ListViewModelFactory by instance()

    private lateinit var binding: FragmentListBinding
    private val viewModel: ListViewModel by lazy { ViewModelProvider(this, factory)[ListViewModel::class.java] }
    private var activityViewModel: MainViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)
        binding.let {

            it.lifecycleOwner = this
            it.entity = viewModel.entity

            it.recycler.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                addItemDecoration(RecyclerDivider(context, RecyclerDivider.LIGHT))
                adapter = viewModel.adapter
            }
        }

        activity?.let { activityViewModel = ViewModelProvider(it)[MainViewModel::class.java] }

        return binding.root
    }

    //TODO Get List Data
    private fun post() {

        viewModel.run {

            entity.hasData.value = false

            adapter.run {
                clear()
                notifyDataSetChanged()
            }
        }

        lifecycleScope.launch {

            try {

                viewModel.post().let { data ->

                    viewModel.run {

                        entity.hasData.value = true

                        adapter.run {
                            addAll(data.map { ListItem(it).apply { setItemClickListener(itemClickListener) }})
                            notifyDataSetChanged()
                        }
                    }
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val itemClickListener = object : ListItem.ItemClickListener {
        override fun onClick(data: ResponseData.Data) {

        }
    }
}