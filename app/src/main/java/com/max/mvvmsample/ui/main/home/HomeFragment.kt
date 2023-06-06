@file:Suppress("unused")

package com.max.mvvmsample.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.max.mvvmsample.databinding.FragmentHomeBinding
import com.max.mvvmsample.ui.base.BaseFragment
import com.max.mvvmsample.ui.main.MainViewModel
import org.kodein.di.instance

class HomeFragment : BaseFragment() {

    private val appContext by lazy { requireContext().applicationContext }

    private lateinit var binding: FragmentHomeBinding
    private val factory by instance<HomeViewModelFactory>()
    private val viewModel by viewModels<HomeViewModel> { factory }
    private val activityViewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            click = this@HomeFragment
            entity = viewModel.entity
        }

        return binding.root
    }
}