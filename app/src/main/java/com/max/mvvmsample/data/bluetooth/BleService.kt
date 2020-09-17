package com.max.mvvmsample.data.bluetooth

import android.app.*
import android.bluetooth.*
import android.bluetooth.BluetoothDevice.TRANSPORT_LE
import android.bluetooth.BluetoothGatt.GATT_SUCCESS
import android.bluetooth.BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.text.TextUtils
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.max.mvvmsample.R
import com.max.mvvmsample.data.preferences.NOTIFICATION_CHANNEL_VERSION
import com.max.mvvmsample.data.preferences.PreferenceProvider
import com.max.mvvmsample.ui.main.MainActivity
import java.util.*

class BleService : Service() {

    //TODO Set Service
    companion object {

        private const val SERVICE_CHANNEL_ID = "Ble Service Channel"

        private val MAIN_SERVICE = UUID.fromString("Main Service")
        private val NOTIFY_CHARACTERISTIC = UUID.fromString("Notify Characteristic")
    }

    private val preferenceProvider by lazy { PreferenceProvider(applicationContext) }

    private var bluetoothManager: BluetoothManager? = null
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var bluetoothGatt: BluetoothGatt? = null

    private var bleDataCallback: BleDataCallback? = null

    private var isStarting = false
    private var isConnecting = false

    override fun onCreate() {
        super.onCreate()

        init()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (!isStarting) {

            createNotificationChannel()
            startForegroundService()

            isStarting = true
        }

        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent?): IBinder? {
        return BleServiceBinder(this)
    }

    fun init() : Boolean {

        if (!isStarting) {

            if (bluetoothManager == null) {

                bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager

                if (bluetoothManager == null) return false
            }

            if (bluetoothAdapter == null) {

                bluetoothAdapter = bluetoothManager!!.adapter

                if (bluetoothAdapter == null) return false
            }
        }

        return true
    }

    //TODO Set Notification Channel
    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            if (preferenceProvider.getInt(NOTIFICATION_CHANNEL_VERSION) < 1) {

                (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
                    NotificationChannel(SERVICE_CHANNEL_ID, "Notification Channel Name", NotificationManager.IMPORTANCE_HIGH).apply {
                        enableLights(true)
                        enableVibration(true)
                        lockscreenVisibility = Notification.VISIBILITY_PRIVATE
                        setShowBadge(true)
                    }
                )

                preferenceProvider.set(NOTIFICATION_CHANNEL_VERSION, 1)
            }
        }
    }

    //TODO Set Notification
    private fun startForegroundService() {

        val pendingIntent = PendingIntent.getActivity(this@BleService, 0, Intent(this@BleService, MainActivity::class.java), 0)
        val notification = NotificationCompat.Builder(this, SERVICE_CHANNEL_ID).apply {
            color = ContextCompat.getColor(this@BleService, R.color.colorPrimary)
            setSmallIcon(R.drawable.notification_icon)
            setDefaults(NotificationCompat.DEFAULT_ALL)
            setStyle(NotificationCompat.BigTextStyle().bigText("Background Service Content"))
            setContentTitle("Background Service Title")
            setContentText("Background Service Content")
            setContentIntent(pendingIntent)
        }.build()

        startForeground(1, notification)
    }

    fun connect() {

        if (isConnecting) return

        if (bluetoothAdapter == null) return

        //TODO set Mac Address
        "mac".let { mac ->

            if (mac.isBlank()) return

            if (!bluetoothAdapter!!.isDiscovering) bluetoothAdapter!!.startDiscovery()

            bluetoothAdapter!!.getRemoteDevice(mac).let { device ->

                bluetoothGatt = when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> device.connectGatt(this, false, gattCallback, TRANSPORT_LE)
                    else -> device.connectGatt(this, false, gattCallback)
                }
            }
        }
    }

    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)

            if (bluetoothAdapter != null && bluetoothAdapter!!.isDiscovering) {
                bluetoothAdapter!!.cancelDiscovery()
            }

            when (status) {

                GATT_SUCCESS -> {

                    isConnecting = true

                    gatt?.discoverServices()
                }

                else -> connect()
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)

            gatt?.getService(MAIN_SERVICE).let {
                if (status == GATT_SUCCESS && it != null) setNotify(it)
            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?
        ) {
            super.onCharacteristicChanged(gatt, characteristic)

            val receiveData = bytesToHexStringArray(characteristic?.value)

            //TODO Deal Receive Data

            Log.i("BleService", "receive data : ${receiveData.contentToString()}")
        }
    }

    private fun setNotify(
        service: BluetoothGattService
    ) {

        try {

            bluetoothGatt?.setCharacteristicNotification(service.getCharacteristic(NOTIFY_CHARACTERISTIC), true)

            service.getCharacteristic(NOTIFY_CHARACTERISTIC).descriptors?.let {

                if (it.isNotEmpty()) {

                    for (descriptor in it) {

                        bluetoothGatt?.writeDescriptor(descriptor.apply {
                            value = ENABLE_NOTIFICATION_VALUE
                        })
                    }
                }
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun bytesToHexStringArray(
        src: ByteArray?
    ): Array<String> {

        val list: MutableList<String> = emptyList<String>().toMutableList()

        if (src == null || src.isEmpty()) {
            return list.toTypedArray()
        }

        for (b in src) {

            Integer.toHexString(b.toInt().and(0xFF)).run {

                when {
                    length < 2 -> list.add("0$this")
                    else -> list.add(this)
                }
            }
        }

        return list.toTypedArray()
    }

    fun setBleDataCallback(
        bleDataCallback: BleDataCallback
    ) {
        this.bleDataCallback = bleDataCallback
    }
}