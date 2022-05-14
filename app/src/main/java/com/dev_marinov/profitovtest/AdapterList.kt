package com.dev_marinov.profitovtest


import android.content.Context
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class AdapterList(val context: Context, var arrayList: ArrayList<String> = ArrayList(), var recyclerView: RecyclerView)
    : RecyclerView.Adapter<AdapterList.ViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterList.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.rv_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterList.ViewHolder, position: Int) {

        val myPos = position % arrayList.size
        holder.tvId.setText(arrayList[myPos]).toString()







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
        //val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        val tvId: TextView = itemView.findViewById(R.id.tvId)
        val imgArrowLeft: ImageView = itemView.findViewById(R.id.imgArrowLeft)
        val imgArrowRight: ImageView = itemView.findViewById(R.id.imgArrowRight)

    }
    init {






    }


}



