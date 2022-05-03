@file:Suppress("unused")

package com.max.mvvmsample.ui.welcome

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.max.mvvmsample.R
import com.max.mvvmsample.data.repositories.WelcomeRepository.Companion.BACKGROUND_LOCATION_PERMISSION
import com.max.mvvmsample.data.repositories.WelcomeRepository.Companion.PERMISSION
import com.max.mvvmsample.databinding.ActivityWelcomeBinding
import com.max.mvvmsample.ui.base.BaseActivity
import com.max.mvvmsample.ui.main.MainActivity
import com.max.mvvmsample.utils.Coroutines.main
import com.max.mvvmsample.view.dialog.CustomAlertDialog
import com.max.mvvmsample.view.dialog.CustomProgressDialog
import io.reactivex.rxjava3.core.Observable
import org.kodein.di.android.closestDI
import org.kodein.di.instance
import java.util.concurrent.TimeUnit

class WelcomeActivity : BaseActivity() {

    override val di by closestDI()

    private val factory: WelcomeViewModelFactory by instance()

    private val binding: ActivityWelcomeBinding by lazy { DataBindingUtil.setContentView(this, R.layout.activity_welcome) }
    private val viewModel: WelcomeViewModel by lazy { ViewModelProvider(this, factory)[WelcomeViewModel::class.java] }

    private val askPermissionAgainDialog: CustomAlertDialog by lazy {

        //TODO Set Ask Permission Again Dialog
        CustomAlertDialog(this)
            .setPositiveButton("ok") {
                ActivityCompat.requestPermissions(
                    this,
                    viewModel.permissionArray,
                    PERMISSION
                )
            }
    }

    private val askBackgroundLocationPermissionDialog: CustomAlertDialog by lazy {

        //TODO Set Ask Permission Again Dialog
        CustomAlertDialog(this)
            .setPositiveButton("ok") {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                    BACKGROUND_LOCATION_PERMISSION
                )
            }
    }

    private val askBackgroundLocationAgainPermissionDialog: CustomAlertDialog by lazy {

        //TODO Set Ask Permission Again Dialog
        CustomAlertDialog(this)
            .setPositiveButton("ok") {

                startActivity(
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        data = Uri.fromParts("package", packageName, null)
                    }
                )
            }
    }

    //TODO Set Progress Message
    private val progressDialog: CustomProgressDialog by lazy { CustomProgressDialog(this, R.string.app_name) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.lifecycleOwner = this

        viewModel.setStartTime()
    }

    override fun onStart() {
        super.onStart()

        when {

            viewModel.isNeedAskPermission() -> {

                //If permission is not granted, request permission.
                ActivityCompat.requestPermissions(
                    this@WelcomeActivity,
                    viewModel.permissionArray,
                    PERMISSION
                )
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && viewModel.isNeedAskBackgroundLocationPermission() -> {

                //If background location permission is not granted, request permission.
                ActivityCompat.requestPermissions(
                    this@WelcomeActivity,
                    arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                    BACKGROUND_LOCATION_PERMISSION
                )
            }

            else -> checkNotificationChannel()
        }
    }

    //If permission is not granted, ask again.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {

            PERMISSION -> when {
                viewModel.isNeedAskPermission(grantResults) -> main { askPermissionAgainDialog.show() }
                viewModel.isNeedAskBackgroundLocationPermission() -> main { askBackgroundLocationPermissionDialog.show() }
                else -> checkNotificationChannel()
            }

            BACKGROUND_LOCATION_PERMISSION -> when {
                viewModel.isNeedAskBackgroundLocationPermission() -> main { askBackgroundLocationAgainPermissionDialog.show() }
                else -> checkNotificationChannel()
            }

            else -> checkNotificationChannel()
        }
    }

    private fun checkNotificationChannel() {

        when {

            Build.VERSION.SDK_INT < Build.VERSION_CODES.O -> goToNext()

            else -> NotificationManagerCompat.from(applicationContext).let { notificationManager ->

                with(viewModel) {

                    cacheNotificationChannelIdList.addAll(notificationManager.notificationChannels.map { it.id })

                    getNotificationChannelList().forEach { channel ->

                        notificationManager.createNotificationChannel(channel)

                        viewModel.cacheNotificationChannelIdList.removeIf { it == channel.id }
                    }
                }

                with(viewModel.cacheNotificationChannelIdList) {
                    forEach { notificationManager.deleteNotificationChannel(it) }
                }

                goToNext()
            }
        }
    }

    private fun goToNext() {

        Observable.timer(viewModel.getDelayTime(), TimeUnit.MILLISECONDS).subscribe {

            main { startActivity(Intent(this@WelcomeActivity, MainActivity::class.java), ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle()) }

            Observable.timer(1500, TimeUnit.MILLISECONDS).subscribe { if (!isFinishing) main { finish() }  }
        }
    }
}