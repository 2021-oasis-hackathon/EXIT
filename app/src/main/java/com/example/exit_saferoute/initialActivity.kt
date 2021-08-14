package com.example.exit_saferoute

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_initial.*

class initialActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)

        button1.setOnClickListener {
            val itnt = Intent(this@initialActivity, SelectActivity::class.java)
            startActivity(itnt)
        }
    }
}