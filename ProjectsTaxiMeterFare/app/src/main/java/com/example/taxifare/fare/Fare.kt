package com.example.taxifare.fare

import com.example.taxifare.model.FareSettings

object Fare {
    private const val STARTING_FARE = 5000 // giá cước mở cửa
    private const val DISTANCE_RATE_1 = 12000 // giá cước từ 0,3km đến 25km
    private const val DISTANCE_RATE_2 = 10000 // giá cước từ 25km trở lên

    // Tính toán giá cước dựa trên quãng đường (đơn vị mét) và thời gian di chuyển (đơn vị phút)
    fun calculateFare(distance: Float, time: Double, fareSettings: FareSettings): Double {
        val distanceInKm = distance / 1000
        val distanceRate = if (distanceInKm > 25) DISTANCE_RATE_2 else DISTANCE_RATE_1
        val vehicleFare = fareSettings.getFareByVehicleType()
        return (STARTING_FARE + distanceInKm * distanceRate + time * vehicleFare)
    }
}