package com.example.exit_saferoute

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item.view.*
import java.security.PublicKey
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    var firestore : FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var itnt = intent
        val dName: String? = itnt.getStringExtra("name")
        firestore = FirebaseFirestore.getInstance()

        val adapter1 = RecyclerViewAdapter()

        adapter1.setItemClickListener(object : OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val item = adapter1.testroute
                Toast.makeText(v.context, "${item[position].name} ${item[position].address}", Toast.LENGTH_SHORT).show()
                adapter1.notifyDataSetChanged()
            }
        })
        recyclerview.adapter = adapter1
        recyclerview.layoutManager = LinearLayoutManager(this@MainActivity)

    }
    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        //  ArrayList 생성성
        var testroute : ArrayList<ItemForList> = arrayListOf()

        init {  // testroute의 문서를 불러온 뒤 ItemForList로 변환해 ArrayList에 담음
            var itnt = intent
            val dName: String? = itnt.getStringExtra("name")
            firestore?.collection(dName!!)?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                // ArrayList 비워줌
                testroute.clear()

                for (snapshot in querySnapshot!!.documents) {
                    var item = snapshot.toObject(ItemForList::class.java)
                    testroute.add(item!!)
                }
                notifyDataSetChanged()
            }
        }

        // xml파일을 inflate하여 ViewHolder를 생성
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
            return ViewHolder(view)
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        }

        // onCreateViewHolder에서 만든 view와 실제 데이터를 연결
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewHolder = (holder as ViewHolder).itemView

            viewHolder.name.text = testroute[position].name
            viewHolder.address.text = testroute[position].address

            holder.itemView.setOnClickListener {
                itemClickListener.onClick(it, position)
            }
        }
        private lateinit var itemClickListener : OnItemClickListener

        fun setItemClickListener(itemClickListener: OnItemClickListener) {
            this.itemClickListener = itemClickListener
        }
        // 리사이클러뷰의 아이템 총 개수 반환
        override fun getItemCount(): Int {
            return testroute.size
        }
    }
}
interface OnItemClickListener {
    fun onClick(v: View, position: Int)
}