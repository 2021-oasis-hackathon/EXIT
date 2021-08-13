package com.example.exit_saferoute

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.media.JetPlayer
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.popup_item.view.*


class popup_list1 : DialogFragment() {
    val testlist = ArrayList<popup_item1>()
    lateinit var recyclerView1: RecyclerView

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this@popup_list1, 0.9f, 0.9f)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.popup_list1, container, false)
        for (i in 0 until 10) {
            testlist.add(popup_item1("test" + i))
        }
        recyclerView1 = rootView.findViewById(R.id.citylist1!!) as RecyclerView
        recyclerView1.layoutManager = LinearLayoutManager(requireContext())
        recyclerView1.adapter = popupadapter(requireContext(), testlist)
        return rootView
    }

    //    private val dialog = Dialog(context)
//    fun mydig() {
//
//
//        dialog.setContentView(R.layout.popup_list1)
//        dialog.window!!.setLayout(
//            WindowManager.LayoutParams.MATCH_PARENT,
//            WindowManager.LayoutParams.WRAP_CONTENT
//        )
//        dialog.setCancelable(true)
//        dialog.show()
//
//
//    }
    fun Context.dialogFragmentResize(dialogFragment: DialogFragment, width: Float, height: Float) {
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        if (Build.VERSION.SDK_INT < 30) {

            val display = windowManager.defaultDisplay
            val size = Point()

            display.getSize(size)

            val window = dialogFragment.dialog?.window

            val x = (size.x * width).toInt()
            val y = (size.y * height).toInt()
            window?.setLayout(x, y)

        } else {

            val rect = windowManager.currentWindowMetrics.bounds

            val window = dialogFragment.dialog?.window

            val x = (rect.width() * width).toInt()
            val y = (rect.height() * height).toInt()

            window?.setLayout(x, y)
        }
    }
}

class popupadapter(val context: Context, val itemlist: ArrayList<popup_item1>) :
    RecyclerView.Adapter<popupadapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.popup_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val viewHolder = (holder as ViewHolder).itemView

        viewHolder.popuptext.text = itemlist[position].name

    }

    override fun getItemCount(): Int {
        return itemlist.size
    }
}