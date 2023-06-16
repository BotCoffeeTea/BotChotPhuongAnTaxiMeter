package com.example.taxifare.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice

class BluetoothDevice(val name: String, val address: String) {

    fun toBluetoothDevice(): BluetoothDevice {
        val adapter = BluetoothAdapter.getDefaultAdapter()
        return adapter.getRemoteDevice(address)
    }
}