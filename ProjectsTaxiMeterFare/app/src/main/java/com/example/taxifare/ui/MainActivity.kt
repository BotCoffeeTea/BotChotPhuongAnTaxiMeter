package com.example.taxifare.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.taxifare.R
import com.example.taxifare.map.CarLocationService
import com.example.taxifare.map.MapUtils
import com.example.taxifare.model.Trip
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), CarLocationService.LocationCallback {

    private lateinit var carLocationService: CarLocationService
    private var tripStarted: Boolean = false
    private var tripStartedTime: Long = 0
    private var tripEndedTime: Long = 0
    private var tripDistanceMeters: Float = 0f
    private var tripPrice: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Khởi tạo đối tượng CarLocationService để theo dõi vị trí của xe
        carLocationService = CarLocationService(this)
        
        // Hiển thị bản đồ
        MapUtils.showMap(supportFragmentManager, map)
        
        // Bắt đầu chuyến đi khi nhấn nút "Start"
        startButton.setOnClickListener {
            if (!tripStarted) {
                tripStarted = true
                tripStartedTime = Calendar.getInstance().timeInMillis
                carLocationService.startLocationUpdates()
                startButton.text = "End Trip"
            } else {
                tripStarted = false
                tripEndedTime = Calendar.getInstance().timeInMillis
                carLocationService.stopLocationUpdates()
                val trip = Trip(tripDistanceMeters, tripStartedTime, tripEndedTime, tripPrice)
                // Lưu thông tin về chuyến đi vào cơ sở dữ liệu
                // ...
                startButton.text = "Start Trip"
                tripDistanceTextView.text = "0.0 km"
                tripTimeTextView.text = "0 min"
                tripPriceTextView.text = "$0.0"
            }
        }
    }

    override fun onLocationUpdate(location: LatLng) {
        MapUtils.updateCarLocationOnMap(map, location)
        // Tính toán quãng đường        
		tripDistanceMeters = MapUtils.calculateDistance(carLocationService.getLastKnownLocation(), location)

        // Cập nhật thông tin về quãng đường
        tripDistanceTextView.text = "%.2f km".format(tripDistanceMeters / 1000)

        // Tính toán giá cước dựa trên quãng đường và thời gian đi
        val tripTimeMinutes = (Calendar.getInstance().timeInMillis - tripStartedTime) / 60000
        tripPrice = Fare.calculateFare(tripDistanceMeters, tripTimeMinutes.toDouble())

        // Cập nhật thông tin về thời gian di chuyển và giá cước
        tripTimeTextView.text = "%d min".format(tripTimeMinutes)
        tripPriceTextView.text = "$%.2f".format(tripPrice)
    }

    override fun onResume() {
        super.onResume()
        MapUtils.requestLocationPermissions(this)
    }

    override fun onPause() {
        super.onPause()
        carLocationService.stopLocationUpdates()
    }
}