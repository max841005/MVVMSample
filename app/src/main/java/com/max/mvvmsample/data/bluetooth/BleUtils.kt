package com.max.mvvmsample.data.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanSettings
import android.bluetooth.le.ScanSettings.*
import android.content.Context
import android.content.Context.BLUETOOTH_SERVICE
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.M

class BleUtils(
    context: Context
) {

    private val appContext = context.applicationContext

    val adapter: BluetoothAdapter by lazy { (appContext.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager).adapter }

    val scanner: BluetoothLeScanner by lazy { adapter.bluetoothLeScanner }

    val scanSetting: ScanSettings = Builder()
        .setScanMode(SCAN_MODE_LOW_LATENCY)
        .apply {

            if (SDK_INT >= M) {
                setCallbackType(CALLBACK_TYPE_ALL_MATCHES)
                setMatchMode(MATCH_MODE_AGGRESSIVE)
                setNumOfMatches(MATCH_NUM_MAX_ADVERTISEMENT)
            }

        }.build()
}