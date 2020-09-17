package com.max.mvvmsample.ui.main

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.max.mvvmsample.R
import com.max.mvvmsample.data.bluetooth.BleDataCallback
import com.max.mvvmsample.data.bluetooth.BleService
import com.max.mvvmsample.data.bluetooth.BleServiceBinder
import com.max.mvvmsample.databinding.ActivityMainBinding
import com.max.mvvmsample.ui.base.BaseActivity
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class MainActivity : BaseActivity() {

    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()

    private val binding: ActivityMainBinding by lazy { DataBindingUtil.setContentView(this, R.layout.activity_main) }
    private val viewModel: MainViewModel by lazy { ViewModelProvider(this, factory)[MainViewModel::class.java] }

    private val fragment: NavHostFragment by lazy { supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment }

    private var bleService: BleService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.let {
            it.lifecycleOwner = this
            it.click = this
            it.entity = viewModel.entity
            it.bottomMenu.setupWithNavController(this.fragment.navController)
        }

        fragment.navController.addOnDestinationChangedListener(destinationChangedListener)

        //TODO Start Service
//        startService(Intent(applicationContext, BleService::class.java))
//
//        bindService(Intent(this, BleService::class.java), serviceConnection, BIND_AUTO_CREATE)
    }

    override fun onResume() {
        super.onResume()

        viewModel.setCurrentDate()
    }

    private val destinationChangedListener = NavController.OnDestinationChangedListener { _, destination, _ -> viewModel.setNowFragment(destination) }

    override fun onClick(view: View?) {

        when (view) {
            binding.previous -> viewModel.setPreviousDate()
            binding.next -> viewModel.setNextDate()
            binding.date -> viewModel.getDatePicker().show(supportFragmentManager, this.toString())
        }
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

            if (bleService == null) bleService = (service as BleServiceBinder).service

            if (bleService != null) {

                if (bleService!!.init()) {

                    bleService!!.setBleDataCallback(bleDataCallback)

                    bleService!!.connect()
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }
    }

    private val bleDataCallback = object : BleDataCallback {
        override fun onDataReceive(data: String) {

        }
    }
}