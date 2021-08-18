package com.example.exit_saferoute

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_initial.*
import kotlinx.android.synthetic.main.activity_initial.gjbtn
import kotlinx.android.synthetic.main.activity_initial.jbbtn
import kotlinx.android.synthetic.main.activity_initial.jnbtn
import kotlinx.android.synthetic.main.item.*
import kotlinx.android.synthetic.main.popup_list2.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.*


class InitialActivity : AppCompatActivity() {
    var getLatitude = 0.0
    var getLongitude = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)
        Toast.makeText(baseContext, "사용 시 위치정보기능을 켜주세요!", Toast.LENGTH_LONG).show()
        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val database = Firebase.database
        val geocoder = Geocoder(this@InitialActivity, Locale.KOREAN)
        val latRef = database.getReference("lat")
        val lngRef = database.getReference("lng")
        val tokenRef = database.getReference("token")
        val gpsLocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                val provider: String = location.provider
                val longitude: Double = location.longitude
                val latitude: Double = location.latitude
                val altitude: Double = location.altitude

            }

            //아래 3개함수는 형식상 필수부분
            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }
        val isGPSEnabled: Boolean = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled: Boolean =
            lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (Build.VERSION.SDK_INT >= 23 &&
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@InitialActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                0
            )
        } else {
            when {
                isNetworkEnabled -> {
                    lm.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        1,
                        1F,
                        gpsLocationListener
                    )
                    val location =
                        lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) //인터넷기반으로 위치를 찾음
                    if (location != null) {
                        getLongitude = location!!.longitude
                        getLatitude = location!!.latitude
                        //Log.d("testTag", ""+getLatitude+","+getLongitude)
                        latRef.setValue(getLatitude)
                        lngRef.setValue(getLongitude)

                    }
                }
                isGPSEnabled -> {
                    lm.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        1,
                        1F,
                        gpsLocationListener
                    )
                    val location =
                        lm.getLastKnownLocation(LocationManager.GPS_PROVIDER) //GPS 기반으로 위치를 찾음
                    if (location != null) {
                        getLongitude = location?.longitude!!
                        getLatitude = location.latitude
                        //Log.d("testTag", ""+getLatitude+","+getLongitude)
                        latRef.setValue(getLatitude)
                        lngRef.setValue(getLongitude)
                    }
                }
            }
        }
        lm.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            1000,
            1F,
            gpsLocationListener
        )
        lm.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            1000,
            1F,
            gpsLocationListener
        )
        CoroutineScope(Dispatchers.IO).launch {
            while(true){
                lm.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    1000,
                    1F,
                    gpsLocationListener
                )
                lm.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000,
                    1F,
                    gpsLocationListener
                )
                val location =
                    lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) //인터넷기반으로 위치를 찾음
                if (location != null) {
                    getLongitude = location!!.longitude
                    getLatitude = location!!.latitude
                    //Log.d("testTag", ""+getLatitude+","+getLongitude)
                    latRef.setValue(getLatitude)
                    lngRef.setValue(getLongitude)}
            }
        }
// 디바이스 토큰 값 확인
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log
            val msg = getString(R.string.msg_token_fmt, token)
            Log.d("TAG", msg)
            tokenRef.setValue(msg)

        })

        var checkedItemPosition = 0
        val arrayGj = arrayOf("북구", "남구", "동구", "서구", "광산구")
        val arrayJn = arrayOf(
            "목포시",
            "여수시",
            "순천시",
            "나주시",
            "광양시",
            "담양군",
            "곡성군",
            "구례군",
            "고흥군",
            "보성군",
            "화순군",
            "장흥군",
            "강진군",
            "해남군",
            "영암군",
            "무안군",
            "함평군",
            "영광군",
            "장성군",
            "완도군",
            "진도군",
            "신안군"
        )
        val arrayJb = arrayOf(
            "전주시",
            "군산시",
            "익산시",
            "정읍시",
            "남원시",
            "김제시",
            "완주군",
            "진안군",
            "무주군",
            "장수군",
            "임실군",
            "순창군",
            "고창군",
            "부안군"
        )
        gjbtn.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("세부 구역")
                .setSingleChoiceItems(
                    arrayGj,
                    checkedItemPosition,
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, which: Int) {
                            checkedItemPosition = which
                        }
                    })
                .setPositiveButton("ok", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        val itnt = Intent(this@InitialActivity, MainActivity::class.java)
                        itnt.putExtra("name", arrayGj[checkedItemPosition])
                        startActivity(itnt)
                    }
                }).setNegativeButton("cancel", null)
                .show()
        }
        jnbtn.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("세부 구역")
                .setSingleChoiceItems(
                    arrayJn,
                    checkedItemPosition,
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, which: Int) {
                            checkedItemPosition = which
                        }
                    })
                .setPositiveButton("ok", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        val itnt = Intent(this@InitialActivity, MainActivity::class.java)
                        itnt.putExtra("name", arrayJn[checkedItemPosition])
                        startActivity(itnt)
                    }
                }).setNegativeButton("cancel", null)
                .show()
        }
        jbbtn.setOnClickListener {
            /** dialog 연습 부분
            //            bundle.putString("hn", "3")
            //            val dialog = popup_list1()
            //            dialog.arguments = bundle
            //            dialog.show(supportFragmentManager, "test")
             **/
            AlertDialog.Builder(this)
                .setTitle("세부 구역")
                .setSingleChoiceItems(
                    arrayJb,
                    checkedItemPosition,
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, which: Int) {
                            checkedItemPosition = which
                        }
                    })
                .setPositiveButton("ok", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        val itnt = Intent(this@InitialActivity, MainActivity::class.java)
                        itnt.putExtra("name", arrayJb[checkedItemPosition])
                        startActivity(itnt)
                    }
                }).setNegativeButton("cancel", null).show()
        }

        button1.setOnClickListener {
            val isGPSEnabled: Boolean = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled: Boolean =
                lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@InitialActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    0
                )
            } else {
                when {
                    isNetworkEnabled -> {
                        lm.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            1,
                            1F,
                            gpsLocationListener
                        )
                        val location =
                            lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) //인터넷기반으로 위치를 찾음
                        if (location != null) {
                            getLongitude = location!!.longitude
                            getLatitude = location!!.latitude
                            //Log.d("testTag", ""+getLatitude+","+getLongitude)
                            latRef.setValue(getLatitude)
                            lngRef.setValue(getLongitude)
                            val result = geocoder.getFromLocation(getLatitude, getLongitude, 1)
                            Log.d("testTag", "" + result.get(0).getAddressLine(0).split(" ")[2])
                            val addressResult = result.get(0).getAddressLine(0).split(" ")[2]

                            val itnt = Intent(this@InitialActivity, EmergencyMap::class.java)
                            itnt.putExtra("position", addressResult)
                            startActivity(itnt)
                        }
                    }
                    isGPSEnabled -> {
                        lm.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            1,
                            1F,
                            gpsLocationListener
                        )
                        val location =
                            lm.getLastKnownLocation(LocationManager.GPS_PROVIDER) //GPS 기반으로 위치를 찾음
                        if (location != null) {
                            getLongitude = location?.longitude!!
                            getLatitude = location.latitude
                            //Log.d("testTag", ""+getLatitude+","+getLongitude)
                            latRef.setValue(getLatitude)
                            lngRef.setValue(getLongitude)
                            val result = geocoder.getFromLocation(getLatitude, getLongitude, 1)
                            Log.d("testTag", "" + result.get(0).getAddressLine(0).split(" ")[2])
                            val addressResult = result.get(0).getAddressLine(0).split(" ")[2]

                            val itnt = Intent(this@InitialActivity, EmergencyMap::class.java)
                            itnt.putExtra("position", addressResult)
                            startActivity(itnt)
                        }
                    }
                }
            }


        }
    }
}




