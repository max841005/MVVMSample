package com.max.mvvmsample.data.bluetooth

import android.os.Binder

class BleServiceBinder(
    bleService: BleService
) : Binder() {
    internal val service: BleService = bleService
}