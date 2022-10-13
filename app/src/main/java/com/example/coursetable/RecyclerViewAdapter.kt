package com.example.coursetable

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter internal constructor(private val itemsList: List<Items>) :
    RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {


    class MyViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        var courseName: TextView = itemView.findViewById(R.id.tv_courseName)
        var courseLocation: TextView = itemView.findViewById(R.id.tv_courseLocation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemsList[position]
        holder.courseName.text = item.courseName
        holder.courseLocation.text = item.courseLocation
        holder.itemView.setOnClickListener{
                view -> clickListener!!.onClick(view,item,position)
        }
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    interface ClickListener<T> {
        fun onClick(view: View?, data: T, position: Int)
    }

    private var clickListener: ClickListener<Items>? = null

    fun setOnItemClickListener(clickListener: ClickListener<Items>) {
        this.clickListener = clickListener
    }
}