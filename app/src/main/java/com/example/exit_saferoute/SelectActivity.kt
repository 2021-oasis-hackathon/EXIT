package com.example.exit_saferoute

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.popup_list2.*

class SelectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popup_list2)
        val bundle: Bundle = Bundle()
        gjbtn.setOnClickListener {
            bundle.putString("hn", "1")
            val dialog = popup_list1()
            dialog.arguments = bundle
            dialog.show(supportFragmentManager, "test")
        }
        jnbtn.setOnClickListener {
            bundle.putString("hn", "2")
            val dialog = popup_list1()
            dialog.arguments = bundle
            dialog.show(supportFragmentManager, "test")
        }
        jbbtn.setOnClickListener {
            bundle.putString("hn", "3")
            val dialog = popup_list1()
            dialog.arguments = bundle
            dialog.show(supportFragmentManager, "test")
        }
    }
}