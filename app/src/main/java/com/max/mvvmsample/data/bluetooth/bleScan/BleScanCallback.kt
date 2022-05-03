package com.max.mvvmsample.data.bluetooth.bleScan

interface BleScanCallback {

    fun onDeviceFound(entity: DeviceScanEntity)
    fun onScanFinish()

    data class DeviceScanEntity(
        val name: String = "",
        val macAddress: String = ""
    )
}