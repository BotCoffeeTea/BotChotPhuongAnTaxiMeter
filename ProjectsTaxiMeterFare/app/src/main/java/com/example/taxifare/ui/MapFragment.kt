package com.example.taxifare.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.taxifare.R
import com.example.taxifare.model.Taxi
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_map.*

class MapFragment :Fragment(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var currentLocation: Location? = null
    private var distance: Double = 0.0
    private var fare: Double = 0.0
    private var waitingTime: Int = 0
    private var startTime: Long = 0
    private var endTime: Long = 0
    private val args: MapFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_map, container, false)
        mapView = rootView.findViewById(R.id.map_view)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Thiết lập fusedLocationProviderClient và locationRequest
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(1000)
            .setFastestInterval(500)

        // Bắt đầutheo dõi vị trí và tính toán giá cước
        startTrackingLocation()
        startTime = System.currentTimeMillis()

        // Xử lý sự kiện khi nhấn nút kết thúc
        end_button.setOnClickListener {
            endTrackingLocation()
            endTime = System.currentTimeMillis()
            val timeInSeconds = (endTime - startTime) / 1000
            val farePerKm = args.taxi.fare
            val totalFare = fare + (distance * farePerKm) + (waitingTime * 500.0)
            val action = MapFragmentDirections.actionMapFragmentToFareFragment(args.taxi.copy(fare = totalFare))
            findNavController().navigate(action)
        }
    }

    // Thiết lập map khi sẵn sàng
    override fun onMapReady(map: GoogleMap?) {
        map?.let {
            googleMap = it
            googleMap.isMyLocationEnabled = true
        }
    }

    // Bắt đầu theo dõi vị trí và tính toán giá cước
    private fun startTrackingLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
           return
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())

        // Tính toán khoảng cách và giá cước
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    if (currentLocation == null) {
                        currentLocation = location
                        googleMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(location.latitude, location.longitude),
                                DEFAULT_ZOOM
                            )
                        )
                    } else {
                        val lastLocation = currentLocation
                        currentLocation = location
                        val newLocation = Location("")
                        newLocation.latitude = location.latitude
                        newLocation.longitude = location.longitude
                        val distanceInMeters = lastLocation?.distanceTo(newLocation)
                        distance += distanceInMeters?.div(1000.0) ?: 0.0
                        fare = distance * args.taxi.fare
                        updateUI()
                    }
                }
            }
        }
    }

    // Kết thúc theo dõi vị trí
    private fun endTrackingLocation() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    // Cập nhật giao diện người dùng
    private fun updateUI() {
        distance_text_view.text = String.format("%.1f km", distance)
        fare_text_view.text = String.format("%.1f đ", fare)
        waiting_time_text_view.text = String.format("%d s", waitingTime)
    }

    // Hàm xử lý khi yêu cầu cấp quyền vị trí được từ chối
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    startTrackingLocation()
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.location_permission_denied),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return
            }
            else -> {
            }
        }
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
        private const val DEFAULT_ZOOM = 15f
    }
}