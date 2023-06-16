package com.example.taxifare.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import java.io.IOException
import java.util.*

private const val UUID_STRING = "00001101-0000-1000-8000-00805F9B34FB"

class BluetoothHelper(private val device: BluetoothDevice) {

    private val uuid = UUID.fromString(UUID_STRING)
    private var socket: BluetoothSocket? = null

    // Thực hiện kết nối với máy in qua Bluetooth
    fun connect(): Boolean {
        var connected = false
        val adapter = BluetoothAdapter.getDefaultAdapter()
        try {
            socket = device.createRfcommSocketToServiceRecord(uuid)
            socket?.connect()
            connected = true
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return connected
    }

    // Gửi dữ liệu đến máy in qua Bluetooth
    fun send(data: ByteArray): Boolean {
        var success = false
        try {
            socket?.outputStream?.write(data)
            success = true
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return success
    }

    // Đóng kếtnối Bluetooth
    fun disconnect() {
        try {
            socket?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}