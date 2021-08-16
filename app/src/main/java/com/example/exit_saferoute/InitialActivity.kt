package com.example.exit_saferoute

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_initial.*

class InitialActivity : AppCompatActivity() {
    val TAG: String = "tokenTest"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)
//        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
//            if (!task.isSuccessful) {
//                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
//                return@OnCompleteListener
//            }
//
//            // Get new FCM registration token
//            val token = task.result
//
//            // Log and toast
//            val msg = getString(R.string.msg_token_fmt, token)
//            Log.d(TAG, msg)
//            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
//        })

        button1.setOnClickListener {
            val itnt = Intent(this@InitialActivity, SelectActivity::class.java)
            startActivity(itnt)
        }
    }

}