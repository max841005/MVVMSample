package com.max.mvvmsample.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.max.mvvmsample.R
import com.max.mvvmsample.databinding.ActivityWelcomeBinding
import com.max.mvvmsample.databinding.LayoutProgressBinding
import com.max.mvvmsample.ui.base.BaseActivity
import com.max.mvvmsample.ui.main.MainActivity
import com.max.mvvmsample.view.CustomAlertDialog
import com.max.mvvmsample.view.progressDialog
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class WelcomeActivity : BaseActivity() {

    override val kodein by kodein()

    private val factory: WelcomeViewModelFactory by instance()

    private val binding: ActivityWelcomeBinding by lazy { DataBindingUtil.setContentView(this, R.layout.activity_welcome) }
    private val viewModel: WelcomeViewModel by lazy { ViewModelProvider(this, factory)[WelcomeViewModel::class.java] }

    //TODO Set Progress Message
    private val progressDialog: AlertDialog by lazy { progressDialog(R.string.app_name) }

    //TODO Set PopupWindow
    private val popupWindowView: View by lazy { View.inflate(this, R.layout.layout_progress, null) }
    private val popupWindowBinding: LayoutProgressBinding by lazy { DataBindingUtil.bind(popupWindowView)!! }
    private val popupWindowPopupWindow: PopupWindow by lazy {
        PopupWindow(popupWindowView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, false)
    }

    private val askPermissionAgainDialog: CustomAlertDialog by lazy {

        //TODO Set Ask Permission Again Dialog
        CustomAlertDialog(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.lifecycleOwner = this

        viewModel.setStartTime()
    }

    override fun onStart() {
        super.onStart()

        //TODO Ask Bluetooth

        when {

            viewModel.isNeedAskPermission -> {

                //If permission is not granted, request permission.
                ActivityCompat.requestPermissions(
                    this,
                    viewModel.permissionArray,
                    viewModel.sendRequestCode
                )
            }

            else -> goToNext()
        }
    }

    //If permission is not granted, ask again.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when {

            viewModel.isNeedAskPermission(requestCode, grantResults) -> if (!askPermissionAgainDialog.isShowing()) askPermissionAgainDialog.show()

            else -> goToNext()
        }
    }

    override fun onClick(view: View?) {

    }

    private fun goToNext() {

        viewModel.handler.postDelayed({

            startActivity(Intent(this, MainActivity::class.java), ActivityOptionsCompat.makeSceneTransitionAnimation(this@WelcomeActivity).toBundle())

            viewModel.handler.postDelayed({ if (!isFinishing) finish() }, 1500)

        }, viewModel.getDelayTime())
    }
}