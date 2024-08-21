package com.example.notyoutube

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.notyoutube.databinding.ItemViewBinding

class dataAdapter(var dataList: ArrayList<dataModel>, var context: Context) :
    RecyclerView.Adapter<dataAdapter.MyViewHolder>() {


    inner class MyViewHolder(var binding: ItemViewBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemViewBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    private var selectedPosition = 0
    var onItemClick : ((Int) -> Unit)? = null   // click listener for an item (it is a lambda function)
    override fun onBindViewHolder(holder: MyViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.binding.typeView.text = dataList[position].viewType
        if(selectedPosition == position)
            holder.binding.typeView.setTextColor(ContextCompat.getColor(context, R.color.white))
        else
            holder.binding.typeView.setTextColor(ContextCompat.getColor(context, R.color.gray))  // default color grey

        holder.binding.root.setOnClickListener{
            notifyItemChanged(selectedPosition) // change color of previous selected item to grey
            selectedPosition = position
            holder.binding.typeView.setTextColor(ContextCompat.getColor(context, R.color.white)) // when clicked, change to white
            onItemClick?.invoke(holder.adapterPosition) // invoke click listener
        }
    }
}