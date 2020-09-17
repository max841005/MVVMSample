package com.max.mvvmsample.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.max.mvvmsample.R
import com.max.mvvmsample.databinding.FragmentHomeBinding
import com.max.mvvmsample.ui.main.MainViewModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class HomeFragment : Fragment(), KodeinAware, View.OnClickListener {

    override val kodein by kodein()
    private val factory: HomeViewModelFactory by instance()

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by lazy { ViewModelProvider(this, factory)[HomeViewModel::class.java] }
    private var activityViewModel: MainViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.let {
            it.lifecycleOwner = this
            it.click = this
            it.entity = viewModel.entity
        }

        activity?.let { activityViewModel = ViewModelProvider(it)[MainViewModel::class.java] }

        return binding.root
    }

    override fun onClick(p0: View?) {

    }
}