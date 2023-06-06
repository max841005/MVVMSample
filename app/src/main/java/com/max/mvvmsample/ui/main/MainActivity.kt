@file:Suppress("unused")

package com.max.mvvmsample.ui.main

import android.bluetooth.BluetoothAdapter
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.max.mvvmsample.R
import com.max.mvvmsample.data.bluetooth.bleService.BleService
import com.max.mvvmsample.data.bluetooth.bleService.BleServiceBinder
import com.max.mvvmsample.databinding.ActivityMainBinding
import com.max.mvvmsample.ui.base.BaseActivity
import com.max.mvvmsample.utils.toDatePicker
import com.max.mvvmsample.utils.toLocalDateTime
import org.kodein.di.instance

class MainActivity : BaseActivity() {

    private val binding: ActivityMainBinding by lazy { DataBindingUtil.setContentView(this, R.layout.activity_main) }
    private val factory: MainViewModelFactory by instance()
    private val viewModel by viewModels<MainViewModel> { factory }

    private val fragment: NavHostFragment by lazy { supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment }

    private var bleService: BleService? = null

    private val activityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){

        if (it.resultCode == RESULT_OK) {
            startBleService()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(binding) {
            lifecycleOwner = this@MainActivity
            click = this@MainActivity
            entity = viewModel.entity
            bottomMenu.setupWithNavController(fragment.findNavController())
        }

        fragment.navController.addOnDestinationChangedListener { _, destination, bundle ->
            viewModel.destinationChange(destination)
        }

        //TODO Start Service
        //checkBluetooth()
    }

    override fun onClick(view: View) {
        super.onClick(view)

        when (view) {
            binding.previous -> viewModel.setPreviousDate()
            binding.next -> viewModel.setNextDate()
            binding.date -> showDatePicker()
        }
    }

    private fun showDatePicker() {

        viewModel.currentDate.value!!.toDatePicker(true).apply {
            addOnPositiveButtonClickListener { viewModel.currentDate.value = it.toLocalDateTime().toLocalDate() }
        }.show(supportFragmentManager, this.toString())
    }

    private fun checkBluetooth() {

        when {
            viewModel.isBluetoothEnabled() -> startBleService()
            else -> activityLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
        }
    }

    private fun startBleService() {

        ContextCompat.startForegroundService(applicationContext, Intent(applicationContext, BleService::class.java))

        bindService(Intent(applicationContext, BleService::class.java), bleServiceConnection, BIND_AUTO_CREATE)
    }

    private val bleServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {

            (service as BleServiceBinder).service.let {
                bleService = it
                it.connect()
            }
        }

        override fun onServiceDisconnected(p0: ComponentName?) {}
    }

    private fun unbindBleService() {

        runCatching {
            unbindService(bleServiceConnection)
        }.onFailure {
            it.printStackTrace()
        }
    }
}