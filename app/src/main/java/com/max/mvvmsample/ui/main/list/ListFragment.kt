@file:Suppress("unused")

package com.max.mvvmsample.ui.main.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.max.mvvmsample.databinding.FragmentListBinding
import com.max.mvvmsample.ui.base.BaseFragment
import com.max.mvvmsample.ui.main.MainViewModel
import com.max.mvvmsample.view.RecyclerDivider
import com.max.mvvmsample.view.snackbar
import org.kodein.di.instance

class ListFragment : BaseFragment() {

    private val appContext by lazy { requireContext().applicationContext }

    private lateinit var binding: FragmentListBinding
    private val factory by instance<ListViewModelFactory>()
    private val viewModel by viewModels<ListViewModel> { factory }
    private val activityViewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentListBinding.inflate(inflater, container, false).apply {

            lifecycleOwner = viewLifecycleOwner
            click = this@ListFragment
            entity = viewModel.entity

            with(recycler) {
                layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                setHasFixedSize(true)
                addItemDecoration(RecyclerDivider(requireContext(), RecyclerDivider.Color.GRAY))
                adapter = viewModel.adapter
            }
        }

        with(viewModel) {
            throwMessage.observe(viewLifecycleOwner) { showThrowMessage(it) }
        }

        return binding.root
    }

    private fun showThrowMessage(
        message: String
    ) {

        if (message.isBlank()) return

        binding.root.snackbar(message)

        viewModel.throwMessage.value = ""
    }
}