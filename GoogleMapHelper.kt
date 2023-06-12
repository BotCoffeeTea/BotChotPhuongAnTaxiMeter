class GoogleMapHelper(private val context: Context, private val googleMap: GoogleMap) {

    private var startMarker: Marker? = null

    private var endMarker: Marker? = null

    fun setOnMapClickListener(listener: (LatLng) -> Unit) {

        googleMap.setOnMapClickListener { latLng ->

            listener(latLng)

            updateMarkers(latLng)

        }

    }

    fun calculateDistance(destLatLng: LatLng): Double {

        val startLatLng = startMarker?.position ?: return 0.0

        val results = FloatArray(1)

        Location.distanceBetween(

            startLatLng.latitude,

            startLatLng.longitude,

            destLatLng.latitude,

            destLatLng.longitude,

            results

        )

        return results[0].toDouble() / 1000

    }

    private fun updateMarkers(latLng: LatLng) {

        if (startMarker == null) {

            startMarker = googleMap.addMarker(

                MarkerOptions()

                    .position(latLng)

                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

            )

        } else if (endMarker == null) {

            endMarker = googleMap.addMarker(

                MarkerOptions()

                    .position(latLng)

                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))

            )

            drawPolyline()

        } else {

            startMarker?.remove()

            endMarker?.remove()

            startMarker = googleMap.addMarker(

                MarkerOptions()

                    .position(endMarker!!.position)

                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

            )

            endMarker = googleMap.addMarker(

                MarkerOptions()

                    .position(latLng)

                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))

            )

            drawPolyline()

        }

    }

    private fun drawPolyline() {

        val startLatLng = startMarker!!.position

        val endLatLng = endMarker!!.position

        googleMap.addPolyline(

            PolylineOptions()

                .add(startLatLng, endLatLng)

                .width(5f)

                .color(Color.RED)

        )

    }

}
