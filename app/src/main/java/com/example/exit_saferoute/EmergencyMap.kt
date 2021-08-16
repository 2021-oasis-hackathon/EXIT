package com.example.exit_saferoute


import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.protobuf.DescriptorProtos
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.util.FusedLocationSource

import java.util.*


class EmergencyMap : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_test)
        val fm = supportFragmentManager

        val mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_fragment, it).commit()
            }
        mapFragment.getMapAsync(this)
        locationSource =
            FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions,
                grantResults
            )
        ) {
            if (!locationSource.isActivated) { // 권한 거부됨
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onMapReady(naverMap: NaverMap) {
        val itnt = intent
        val addressName: String? = itnt.getStringExtra("position")
        val geocoder = Geocoder(this@EmergencyMap, Locale.KOREAN)
        val results = geocoder.getFromLocationName(addressName, 1)
        val latlng = LatLng(results[0].latitude, results[0].longitude)
        val result2 = geocoder.getFromLocationName("광주광역시 북구 용봉로 77", 1)
        val latlng2 = LatLng(result2[0].latitude, result2[0].longitude)

        this.naverMap = naverMap

        val locationOverlay = naverMap.locationOverlay

        locationOverlay.isVisible = true
        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.Face
        naverMap.uiSettings.isLocationButtonEnabled = true
        val marker = Marker()
        marker.position = latlng
        marker.map = naverMap

        val marker2 = Marker()
        marker2.position = latlng2
        marker2.map = naverMap

        val path = PathOverlay()
        path.color = Color.GREEN
        naverMap.addOnLocationChangeListener { location ->
            path.coords = listOf(latlng, LatLng(location.latitude, location.longitude))
            path.map = naverMap
        }



    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}
