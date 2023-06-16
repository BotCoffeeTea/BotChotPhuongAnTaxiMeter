package com.example.taxifare.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.example.taxifare.R
import com.example.taxifare.fragment.MapFragment

object MapUtils {

    private const val PACKAGE_GOOGLE_MAPS = "com.google.android.apps.maps"

    fun showMap(fragmentManager: FragmentManager, address: String) {
        val mapFragment = MapFragment.newInstance(address)
        mapFragment.show(fragmentManager, "mapFragment")
    }

    fun openMap(context: Context, address: String) {
        val gmmIntentUri = Uri.parse("geo:0,0?q=$address")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage(PACKAGE_GOOGLE_MAPS)
        if (mapIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(mapIntent)
        } else {
            Toast.makeText(context, R.string.error_google_maps_not_installed, Toast.LENGTH_LONG).show()
        }
    }

    fun isGoogleMapsInstalled(context: Context): Boolean {
        try {
            context.packageManager.getPackageInfo(PACKAGE_GOOGLE_MAPS, 0)
            return true
        } catch (e: Exception) {
            return false
        }
    }

Trong đó, hàm showMap sẽ hiển thị bản đồ trong một Fragment của ứng dụng, còn hàm openMap sẽ mở bản đồ trong ứng dụng Google Maps. Hàm isGoogleMapsInstalled sẽ kiểm tra xem ứng dụng Google Maps đã được cài đặt trên điện thoại hay chưa.