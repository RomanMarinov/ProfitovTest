package com.dev_marinov.profitovtest


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterList(val context: Context, var arrayList: ArrayList<String> = ArrayList())
    : RecyclerView.Adapter<AdapterList.ViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterList.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.rv_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterList.ViewHolder, position: Int) {

        holder.tvId.text = arrayList[position % arrayList.size].toString()

            // слушатели кликов стрелок лево право
        holder.imgArrowLeft.setOnClickListener(View.OnClickListener {
            MainActivity.interFaceAdapter.myInterFaceAdapter("left")
        })
        holder.imgArrowRight.setOnClickListener(View.OnClickListener {
            MainActivity.interFaceAdapter.myInterFaceAdapter("right")
        })
    }

    // Возвращает количество элементов списка.
    override fun getItemCount(): Int {
        return if (arrayList.size == 0) 0 else Integer.MAX_VALUE
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvId: TextView = itemView.findViewById(R.id.tvId)
        val imgArrowLeft: ImageView = itemView.findViewById(R.id.imgArrowLeft)
        val imgArrowRight: ImageView = itemView.findViewById(R.id.imgArrowRight)
    }

}



