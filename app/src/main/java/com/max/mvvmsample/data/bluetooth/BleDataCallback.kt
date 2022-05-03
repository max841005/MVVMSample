@file:Suppress("unused")

package com.max.mvvmsample.data.bluetooth

interface BleDataCallback {
    fun onDataReceive(data: String)
}