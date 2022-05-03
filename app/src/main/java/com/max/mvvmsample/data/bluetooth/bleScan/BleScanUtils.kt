@file:Suppress("unused")

package com.max.mvvmsample.data.bluetooth.bleScan

import android.Manifest.permission.BLUETOOTH_CONNECT
import android.Manifest.permission.BLUETOOTH_SCAN
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.S
import android.os.ParcelUuid
import androidx.core.app.ActivityCompat
import com.max.mvvmsample.data.bluetooth.BleUtils
import io.reactivex.rxjava3.core.Observable.timer
import io.reactivex.rxjava3.disposables.Disposable
import java.util.*
import java.util.concurrent.TimeUnit.SECONDS

class BleScanUtils(
    context: Context,
    private val bleUtils: BleUtils
) {

    private val appContext = context.applicationContext

    var bleScanCallback: BleScanCallback? = null

    private var isScanning = false

    private var observable: Disposable? = null

    fun startScan() {

        isScanning = true

        if (SDK_INT >= S &&
            ActivityCompat.checkSelfPermission(appContext, BLUETOOTH_SCAN) != PERMISSION_GRANTED
        ) {
            return
        }

        //TODO Set ServiceUuid
        bleUtils.scanner.startScan(
            listOf(ScanFilter.Builder().setServiceUuid(ParcelUuid(UUID.fromString("0000XXXX-0000-1000-8000-00805F9B34FB"))).build()),
            bleUtils.scanSetting,
            scanCallback
        )

        observable = timer(15, SECONDS).subscribe { stopScan() }
    }

    private val scanCallback = object : ScanCallback() {

        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)

            result?.let { parseDevice(it) }
        }
    }

    private fun parseDevice(
        result: ScanResult
    ) {

        if (SDK_INT >= S &&
            ActivityCompat.checkSelfPermission(appContext, BLUETOOTH_CONNECT) != PERMISSION_GRANTED
        ) {
            return
        }

        val mac = result.device.address ?: return

        val name = result.device.name ?: return

        bleScanCallback?.onDeviceFound(
            entity = BleScanCallback.DeviceScanEntity(
                name = name,
                macAddress = mac
            )
        )
    }

    fun stopScan() {

        if (isScanning) {

            isScanning = false

            if (SDK_INT < S ||
                ActivityCompat.checkSelfPermission(appContext, BLUETOOTH_SCAN) == PERMISSION_GRANTED
            ) {

                bleUtils.scanner.stopScan(scanCallback)

                if (observable != null) {

                    if (!observable!!.isDisposed) {
                        observable?.dispose()
                    }
                }
            }
        }

        bleScanCallback?.onScanFinish()
    }
}