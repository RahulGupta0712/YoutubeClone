package com.example.notyoutube

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notyoutube.databinding.ActivityItemViewRecyclerViewHomeProfileBinding

class DataAdapterHomeProfile(val context:Context, val datalist : ArrayList<Int>) : RecyclerView.Adapter<DataAdapterHomeProfile.MyViewHolder> (){
    inner class MyViewHolder(var binding:ActivityItemViewRecyclerViewHomeProfileBinding):RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ActivityItemViewRecyclerViewHomeProfileBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }
}