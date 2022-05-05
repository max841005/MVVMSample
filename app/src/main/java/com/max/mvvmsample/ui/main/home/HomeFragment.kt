@file:Suppress("unused")

package com.max.mvvmsample.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.max.mvvmsample.databinding.FragmentHomeBinding
import com.max.mvvmsample.ui.base.BaseFragment
import com.max.mvvmsample.ui.main.MainViewModel
import org.kodein.di.android.x.closestDI
import org.kodein.di.instance

class HomeFragment : BaseFragment() {

    private val appContext by lazy { requireContext().applicationContext }

    override val di by closestDI()
    private val factory: HomeViewModelFactory by instance()

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by lazy { ViewModelProvider(this, factory)[HomeViewModel::class.java] }
    private val activityViewModel: MainViewModel by activityViewModels()

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