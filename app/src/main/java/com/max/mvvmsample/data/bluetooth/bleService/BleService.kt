@file:Suppress("unused")

package com.max.mvvmsample.data.bluetooth.bleService

import android.Manifest
import android.app.PendingIntent
import android.app.Service
import android.bluetooth.BluetoothDevice.TRANSPORT_LE
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGatt.GATT_SUCCESS
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
import android.bluetooth.BluetoothGattService
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.max.mvvmsample.data.bluetooth.BleDataCallback
import com.max.mvvmsample.data.bluetooth.BleUtils
import com.max.mvvmsample.data.config.SERVICE_CHANNEL_ID
import com.max.mvvmsample.data.config.SERVICE_NOTIFICATION_ID
import com.max.mvvmsample.ui.main.MainActivity
import com.max.mvvmsample.utils.Coroutines.main
import io.reactivex.rxjava3.core.Observable
import java.util.*
import java.util.concurrent.TimeUnit

class BleService : Service() {

    //TODO Set Service
    companion object {
        private val MAIN_SERVICE = UUID.fromString("Main Service")
        private val NOTIFY_CHARACTERISTIC = UUID.fromString("Notify Characteristic")
    }

    private val bleUtils by lazy { BleUtils(applicationContext) }

    private var bluetoothGatt: BluetoothGatt? = null

    private var bleDataCallback: BleDataCallback? = null

    private var isStarting = false
    var isConnecting = false
    var isConnected = false

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (!isStarting) {

            startForegroundService()

            isStarting = true
        }

        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent?): IBinder = BleServiceBinder(this)

    //TODO Set Notification
    private fun startForegroundService() {

        startForeground(
            SERVICE_NOTIFICATION_ID,
            NotificationCompat.Builder(this, SERVICE_CHANNEL_ID)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentTitle("Background Service Title")
                .setContentText("Background Service Content")
                .setContentIntent(PendingIntent.getActivity(
                    this,
                    0,
                    Intent(this, MainActivity::class.java),
                    when {
                        Build.VERSION.SDK_INT < Build.VERSION_CODES.M -> PendingIntent.FLAG_UPDATE_CURRENT
                        else -> PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                    }
                ))
                .build()
        )
    }

    fun connect(): Boolean {

        if (isConnected) return false

        //TODO set Mac Address
        val mac = "mac"

        if (mac.isBlank() ||
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }

        bluetoothGatt = when {
            Build.VERSION.SDK_INT < Build.VERSION_CODES.M -> bleUtils.adapter.getRemoteDevice(mac)?.connectGatt(this, false, gattCallback)
            else -> bleUtils.adapter.getRemoteDevice(mac)?.connectGatt(this, false, gattCallback, TRANSPORT_LE)
        }

        return true
    }

    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)

            isConnecting = false

            when (newState) {

                BluetoothGatt.STATE_CONNECTED -> {

                    when (status) {

                        GATT_SUCCESS -> {

                            isConnected = true

                            if (bleUtils.adapter.isEnabled &&
                                Build.VERSION.SDK_INT < Build.VERSION_CODES.S || ActivityCompat.checkSelfPermission(applicationContext,
                                    Manifest.permission.BLUETOOTH_SCAN
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                bleUtils.scanner.stopScan(scanCallback)
                            }

                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S ||
                                ActivityCompat.checkSelfPermission(applicationContext,
                                    Manifest.permission.BLUETOOTH_CONNECT
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                gatt?.discoverServices()
                            }
                        }

                        else -> {

                            isConnected = false

                            //TODO set Mac Address
                            val mac = "mac"

                            if (mac.isNotBlank() &&
                                Build.VERSION.SDK_INT < Build.VERSION_CODES.S || ActivityCompat.checkSelfPermission(applicationContext,
                                    Manifest.permission.BLUETOOTH_SCAN
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {

                                bleUtils.scanner.startScan(
                                    listOf(ScanFilter.Builder().setDeviceAddress(mac).build()),
                                    bleUtils.scanSetting,
                                    scanCallback
                                )
                            }
                        }
                    }
                }

                BluetoothGatt.STATE_DISCONNECTED -> {

                    isConnected = false

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S ||
                        ActivityCompat.checkSelfPermission(applicationContext,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        bluetoothGatt?.close()
                    }

                    //TODO set Mac Address
                    val mac = "mac"

                    if (mac.isNotBlank() &&
                        Build.VERSION.SDK_INT < Build.VERSION_CODES.S || ActivityCompat.checkSelfPermission(applicationContext,
                            Manifest.permission.BLUETOOTH_SCAN
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {

                        bleUtils.scanner.startScan(
                            listOf(ScanFilter.Builder().setDeviceAddress(mac).build()),
                            bleUtils.scanSetting,
                            scanCallback
                        )
                    }
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)

            val service = gatt?.getService(MAIN_SERVICE)

            if (service != null &&
                status == GATT_SUCCESS) {
                Observable.timer(500, TimeUnit.MILLISECONDS).subscribe { main { setNotify(service) } }
            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?
        ) {
            super.onCharacteristicChanged(gatt, characteristic)

            if (characteristic == null) return

            val receiveData = characteristic.value.toHexStringArray()

            //TODO Deal Receive Data
            Log.i("BleService", "receive data : ${receiveData.contentToString()}")
        }
    }

    private fun ByteArray.toHexStringArray(): Array<String> {

        val list = mutableListOf<String>()

        if (this.isEmpty()) return list.toTypedArray()

        for (b in this) {

            Integer.toHexString(b.toInt().and(0xFF)).run {

                when {
                    length < 2 -> list.add("0$this")
                    else -> list.add(this)
                }
            }
        }

        return list.toTypedArray()
    }

    private val scanCallback: ScanCallback = object : ScanCallback() {

        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)

            if (isConnecting) return

            isConnecting = true

            connect()
        }
    }

    private fun setNotify(
        service: BluetoothGattService
    ) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            ActivityCompat.checkSelfPermission(applicationContext,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        bluetoothGatt?.setCharacteristicNotification(service.getCharacteristic(NOTIFY_CHARACTERISTIC), true)

        val descriptorList = service.getCharacteristic(NOTIFY_CHARACTERISTIC).descriptors

        if (descriptorList.isNullOrEmpty()) return

        descriptorList.forEach { bluetoothGatt?.writeDescriptor(it.apply { value = ENABLE_NOTIFICATION_VALUE }) }
    }

    fun setBleDataCallback(
        bleDataCallback: BleDataCallback
    ) {
        this.bleDataCallback = bleDataCallback
    }
}