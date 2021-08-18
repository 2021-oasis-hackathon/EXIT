package com.example.exit_saferoute



import android.graphics.Color
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.android.synthetic.main.item.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.math.log


class EmergencyMap : AppCompatActivity(), OnMapReadyCallback {
    var firestore = FirebaseFirestore.getInstance()
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    var testroute: ArrayList<LatLngData> = arrayListOf()
    var testDouble: Double? = null
    suspend fun getData(collectionPath:String): Task<QuerySnapshot> {
        return firestore.collection(collectionPath).get().addOnSuccessListener { result ->
            // 성공할 경우
            testroute.clear()
            for (document in result) {  // 가져온 문서들은 result에 들어감
                val item = LatLngData(document["lat"] as Double, document["lng"] as Double)
                testroute.add(item)
            }

        }
    }
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
//        val results = geocoder.getFromLocationName("광주광역시 북구 중앙동(누문동 115)", 1)
//        val latlng = LatLng(results[0].latitude, results[0].longitude)

        this.naverMap = naverMap

        val locationOverlay = naverMap.locationOverlay
        locationOverlay.isVisible = true
        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.Face
        naverMap.uiSettings.isLocationButtonEnabled = true
        CoroutineScope(Dispatchers.Main).launch {
            getData(addressName!!).await()
            val path = PathOverlay()
            path.color = Color.GREEN
            naverMap.addOnLocationChangeListener { location ->
                var getLongitude: Double? = location.longitude
                var getLatitude: Double? = location.latitude
                var index = 0
                var comparation: Double = 1000000000000.0 // 임의로 매우 큰 값 설정
                for (i in 0 until testroute.size) {
                    if (comparation > abs(getLatitude!!.minus(testroute[i].lat!!)) + abs(
                            getLongitude!!.minus(
                                testroute[i].lng!!
                            )
                        )
                    ) {
                        comparation = abs(getLatitude!!.minus(testroute[i].lat!!)) + abs(
                            getLongitude!!.minus(testroute[i].lng!!)
                        )
                        index = i
                    }

                }
                val marker = Marker()
                val latlng = LatLng(testroute[index].lat!!, testroute[index].lng!!)
                marker.position = latlng
                marker.map = naverMap
                path.coords = listOf(latlng, LatLng(location.latitude, location.longitude))
                path.map = naverMap
            }

        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}
