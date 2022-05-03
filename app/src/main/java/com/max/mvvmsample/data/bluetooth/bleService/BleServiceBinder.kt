package com.max.mvvmsample.data.bluetooth.bleService

import android.os.Binder

class BleServiceBinder(
    bleService: BleService
) : Binder() {
    internal val service: BleService = bleService
}