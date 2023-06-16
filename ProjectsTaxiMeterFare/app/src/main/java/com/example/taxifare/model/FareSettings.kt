package com.example.taxifare.model

import com.example.taxifare.fare.FareType

data class FareSettings(val vehicleType: String, val fare: Double) {

    companion object {
        val DEFAULT_FARE_SETTINGS = listOf(
            FareSettings("Sedan", 4000.0),
            FareSettings("SUV", 5000.0),
            FareSettings("Van", 6000.0)
        )
    }

    fun getFareByVehicleType(): Double {
        return DEFAULT_FARE_SETTINGS.find { it.vehicleType == vehicleType }?.fare ?: fare
    }

    fun updateFare(newFare: Double) {
        // Tìm kiếm và cập nhật giá cước cho loại xe tương ứng
        DEFAULT_FARE_SETTINGS.find { it.vehicleType == vehicleType }?.let {
            it.fare = newFare
        }
    }
}