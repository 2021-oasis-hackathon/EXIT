//package com.example.exit_saferoute
//
//
//import android.content.Context
//import android.content.Intent
//import android.graphics.Point
//
//import android.os.Build
//import android.os.Bundle
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.view.WindowManager
//
//import androidx.fragment.app.DialogFragment
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import kotlinx.android.synthetic.main.popup_item.view.*
//import kotlinx.android.synthetic.main.popup_item.view.popuptext
//import kotlinx.android.synthetic.main.testitem.view.*
//
//
//class popup_list1 : DialogFragment() {
//    val testlist = ArrayList<popup_item1>()
//    lateinit var recyclerView1: RecyclerView
//
//
//    override fun onResume() {
//        super.onResume()
//        context?.dialogFragmentResize(this@popup_list1, 0.9f, 0.9f)
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        isCancelable = true
//
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val args: Bundle? = arguments
//        val argsValue: String? = args!!.getString("hn")
//        var rootView = inflater.inflate(R.layout.popup_list1, container, false)
//
//        if (argsValue == "1"){
//            testlist.add(popup_item1("북구"))
//            testlist.add(popup_item1("서구"))
//            testlist.add(popup_item1("남구"))
//            testlist.add(popup_item1("동구"))
//            testlist.add(popup_item1("광산구"))
//        }
//
//        else if (argsValue == "2") {
//            testlist.add(popup_item1("목포시"))
//            testlist.add(popup_item1("여수시"))
//            testlist.add(popup_item1("순천시"))
//            testlist.add(popup_item1("나주시"))
//            testlist.add(popup_item1("광양시"))
//            testlist.add(popup_item1("담양군"))
//            testlist.add(popup_item1("곡성군"))
//            testlist.add(popup_item1("구례군"))
//            testlist.add(popup_item1("고흥군"))
//            testlist.add(popup_item1("보성군"))
//            testlist.add(popup_item1("화순군"))
//            testlist.add(popup_item1("장흥군"))
//            testlist.add(popup_item1("강진군"))
//            testlist.add(popup_item1("해남군"))
//            testlist.add(popup_item1("영암군"))
//            testlist.add(popup_item1("무안군"))
//            testlist.add(popup_item1("함평군"))
//            testlist.add(popup_item1("영광군"))
//            testlist.add(popup_item1("장성군"))
//            testlist.add(popup_item1("완도군"))
//            testlist.add(popup_item1("진도군"))
//            testlist.add(popup_item1("신안군"))
//        }
//        else if (argsValue == "3") {
//            testlist.add(popup_item1("전주시 완산구"))
//            testlist.add(popup_item1("전주시 덕진구"))
//            testlist.add(popup_item1("군산시"))
//            testlist.add(popup_item1("익산시"))
//            testlist.add(popup_item1("정읍시"))
//            testlist.add(popup_item1("남원시"))
//            testlist.add(popup_item1("김제시"))
//            testlist.add(popup_item1("완주군"))
//            testlist.add(popup_item1("진안군"))
//            testlist.add(popup_item1("무주군"))
//            testlist.add(popup_item1("장수군"))
//            testlist.add(popup_item1("임실군"))
//            testlist.add(popup_item1("순창군"))
//            testlist.add(popup_item1("고창군"))
//            testlist.add(popup_item1("부안군"))
//        }
//
//        val adapter = popupadapter(requireContext(), testlist)
//        recyclerView1 = rootView.findViewById(R.id.citylist1!!) as RecyclerView
//        recyclerView1.layoutManager = LinearLayoutManager(requireContext())
//        recyclerView1.adapter = adapter
//        adapter.setItemClickListener(object: OnItemClickListener{
//            override fun onClick(v: View, position: Int) {
//                val item = testlist
////                Toast.makeText(v.context, "${item[position].name}", Toast.LENGTH_SHORT).show()
//                val itnt = Intent(activity, MainActivity::class.java)
//                itnt.putExtra("name", item[position].name)
//                startActivity(itnt)
//                adapter.notifyDataSetChanged()
//            }
//        })
//        return rootView
//    }
//
//    //    private val dialog = Dialog(context)
////    fun mydig() {
////
////
////        dialog.setContentView(R.layout.popup_list1)
////        dialog.window!!.setLayout(
////            WindowManager.LayoutParams.MATCH_PARENT,
////            WindowManager.LayoutParams.WRAP_CONTENT
////        )
////        dialog.setCancelable(true)
////        dialog.show()
////
////
////    }
//    fun Context.dialogFragmentResize(dialogFragment: DialogFragment, width: Float, height: Float) {
//        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
//
//        if (Build.VERSION.SDK_INT < 30) {
//
//            val display = windowManager.defaultDisplay
//            val size = Point()
//
//            display.getSize(size)
//
//            val window = dialogFragment.dialog?.window
//
//            val x = (size.x * width).toInt()
//            val y = (size.y * height).toInt()
//            window?.setLayout(x, y)
//
//        } else {
//
//            val rect = windowManager.currentWindowMetrics.bounds
//
//            val window = dialogFragment.dialog?.window
//
//            val x = (rect.width() * width).toInt()
//            val y = (rect.height() * height).toInt()
//
//            window?.setLayout(x, y)
//        }
//    }
//}
//
//class popupadapter(val context: Context, val itemlist: ArrayList<popup_item1>) :
//    RecyclerView.Adapter<popupadapter.ViewHolder>() {
//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        var view = LayoutInflater.from(context).inflate(R.layout.testitem, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val viewHolder = (holder as ViewHolder).itemView
//        holder.itemView.setOnClickListener {
//            itemClickListener.onClick(it, position)
//        }
//        viewHolder.name.text = itemlist[position].name
//
//    }
//    private lateinit var itemClickListener : OnItemClickListener
//
//    fun setItemClickListener(itemClickListener: OnItemClickListener) {
//        this.itemClickListener = itemClickListener
//    }
//    override fun getItemCount(): Int {
//        return itemlist.size
//    }
//}
