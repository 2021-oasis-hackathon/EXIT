package com.example.exit_saferoute

import android.app.NotificationManager
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.popup_list2.*

class SelectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popup_list2)
        val bundle: Bundle = Bundle()
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
            "전주시 완산구",
            "전주시 덕진구",
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
                        val itnt = Intent(this@SelectActivity, MainActivity::class.java)
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
                        val itnt = Intent(this@SelectActivity, MainActivity::class.java)
                        itnt.putExtra("name", arrayJn[checkedItemPosition])
                        startActivity(itnt)
                    }
                }).setNegativeButton("cancel", null)
                .show()
        }
        jbbtn.setOnClickListener {
//            bundle.putString("hn", "3")
//            val dialog = popup_list1()
//            dialog.arguments = bundle
//            dialog.show(supportFragmentManager, "test")
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
                        val itnt = Intent(this@SelectActivity, MainActivity::class.java)
                        itnt.putExtra("name", arrayJb[checkedItemPosition])
                        startActivity(itnt)
                    }
                }).setNegativeButton("cancel", null).show()
        }
        notiBtn.setOnClickListener {

        }
    }
}