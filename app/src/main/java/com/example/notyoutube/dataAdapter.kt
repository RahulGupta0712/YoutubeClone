package com.example.notyoutube

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.notyoutube.ProfileFragments.ProfileCommunityFragment
import com.example.notyoutube.ProfileFragments.ProfileHomeFragment
import com.example.notyoutube.ProfileFragments.ProfileLiveFragment
import com.example.notyoutube.ProfileFragments.ProfilePlaylistsFragment
import com.example.notyoutube.ProfileFragments.ProfileShortsFragment
import com.example.notyoutube.ProfileFragments.ProfileVideosFragment
import com.example.notyoutube.databinding.ItemViewBinding

class dataAdapter(var dataList: ArrayList<dataModel>, var context: Context) :
    RecyclerView.Adapter<dataAdapter.MyViewHolder>() {


    inner class MyViewHolder(var binding: ItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding = ItemViewBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    var onItemClick : ((Int) -> Unit)? = null   // click listener for an item (it is a lambda function)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.typeView.text = dataList.get(position).viewType

        holder.binding.root.setOnClickListener{
            holder.binding.typeView.setTextColor(context.resources.getColor(R.color.white)) // when clicked, change to white
            Handler(Looper.getMainLooper()).postDelayed({
                holder.binding.typeView.setTextColor(context.resources.getColor(R.color.gray))  //after 1 s, back to gray
            }, 1000)
            onItemClick?.invoke(holder.adapterPosition) // invoke click listener
        }
    }
}