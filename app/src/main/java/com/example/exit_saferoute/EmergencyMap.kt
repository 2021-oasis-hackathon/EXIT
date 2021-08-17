package com.example.exit_saferoute


import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.protobuf.DescriptorProtos
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.util.FusedLocationSource

import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log


class EmergencyMap : AppCompatActivity(), OnMapReadyCallback {
    var firestore = FirebaseFirestore.getInstance()
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    var testroute: ArrayList<LatLngData> = arrayListOf()
    var testDouble:Double? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_test)

        firestore.collection("saferoute").get().addOnSuccessListener {
                result ->
            // 성공할 경우
            testroute.clear()
            for (document in result) {  // 가져온 문서들은 result에 들어감
                val item = LatLngData(document["lat"] as Double, document["lng"] as Double)
                testroute.add(item)
            }
            Log.d("testD", ""+testroute[1].lat)
        }


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
        val result2 = geocoder.getFromLocationName("광주광역시 북구 중앙동(누문동 115)", 1)

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
            val currentLatitude = location.latitude
            val currentLongitude = location.longitude
            val currentLatLng = LatLng(currentLatitude, currentLongitude)
        }



    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}
