package com.example.coursetable

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val bt = findViewById<Button>(R.id.btn_back)

        bt.setOnClickListener {
            finish()
        }

//        val msg = intent.getStringExtra("et")
//        val tv = findViewById<TextView>(R.id.textView2)
//        tv.text = msg

        loadData()
//        val itemsList = ArrayList<Items>()
//        for (i in 0..100) {
//            val items = Items("", "")
//            itemsList.add(items)
//        }

        // left data
        val leftItemsList = ArrayList<Items>()
        for (i in 6..20) {
            val items = Items("$i o'clock", "")
            leftItemsList.add(items)
        }

        // top data
        val topItemsList = ArrayList<Items>()
        for (i in 1..7) {
            val items = Items("day $i", "")
            topItemsList.add(items)
        }

//        initRecyclerView(itemsList,R.id.recycleView,true,GridLayoutManager(applicationContext,7))
        //left
        initRecyclerView(leftItemsList,R.id.left_recyclerView,false,GridLayoutManager(applicationContext,1))
        //top
        initRecyclerView(topItemsList,R.id.top_recyclerView,false,GridLayoutManager(applicationContext,7))

        val scrollView0 = findViewById<SyncHorizontalScrollView>(R.id.top_scroll)
        val scrollView1 = findViewById<SyncHorizontalScrollView>(R.id.main_scroll)
        scrollView0.setScrollView(scrollView1)
        scrollView1.setScrollView(scrollView0)

//        val recyclerView = findViewById<RecyclerView>(R.id.recycleView)
//
//        val adapter = RecyclerViewAdapter(itemsList)
//        val layoutManager = GridLayoutManager(applicationContext,7)
//
//        recyclerView.adapter = adapter
//        recyclerView.layoutManager = layoutManager
//        recyclerView.setHasFixedSize(true)
//
//        adapter.setOnItemClickListener(object:RecyclerViewAdapter.ClickListener<Items>{
//            override fun onClick(view: View?, data: Items, position: Int) {
//                Toast.makeText(applicationContext, "Position = $position  data = ${data.courseName}", Toast.LENGTH_LONG).show()
//            }
//        })
    }

    private fun initRecyclerView(itemsList : ArrayList<Items>, viewID : Int, allowedClick: Boolean, layoutManager: RecyclerView.LayoutManager){
        val recyclerView = findViewById<RecyclerView>(viewID)

        val adapter = RecyclerViewAdapter(itemsList)
//        val layoutManager = GridLayoutManager(applicationContext,7)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        adapter.setOnItemClickListener(object:RecyclerViewAdapter.ClickListener<Items>{
            override fun onClick(view: View?, data: Items, position: Int) {
                if (allowedClick){
                    jumpFun(position)
                }
            }
        })
    }

    private fun jumpFun(position:Int) {
        val intent = Intent(this, newClass::class.java)
//        val msg = "message"
        intent.putExtra("position",position)
//        val et = findViewById<EditText>(R.id.editText)
//        intent.putExtra("et",et.text.toString())
        startActivity(intent)

    }

    private fun loadData() {
        // Read from the database
        val database = Firebase.database
        val myRef = database.reference.child("position")
        Log.d("test","myRef: $myRef")
        myRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = snapshot.getValue<Map<String, Any>>()
                if (value != null) {
                    val itemsList = ArrayList<Items>()
                    loadMapData(value,itemsList)
                    initRecyclerView(itemsList,R.id.recycleView,true,GridLayoutManager(applicationContext,7))
                }
                else{
                    Log.d("test","do not have data from position")
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w("test", "Failed to read value.", error.toException())
            }

        })
    }

    private fun loadMapData(value: Map<String, Any>,itemsList : ArrayList<Items>) {

        for (i in 0..104) {
            Log.d("test", "map value is: $i  ${value["$i"]}")
            val courseInfo: Any? = value["$i"]
            if (courseInfo is Map<*, *>) {
                val courseName: String = courseInfo["courseName"] as String
                val location: String = courseInfo["location"] as String
                val item = Items(courseName, location)
                itemsList.add(item)
                Log.d("test", "loadMapData1111: $item ${itemsList[i]}")
            } else {
                val item = Items("", "")
                itemsList.add(item)
            }
        }
    }

}