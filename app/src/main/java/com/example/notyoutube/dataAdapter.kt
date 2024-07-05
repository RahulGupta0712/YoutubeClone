package com.example.notyoutube

import android.content.Context
import android.content.Intent
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

    var onItemClick : ((Int) -> Unit)? = null   // click listener for an item
    inner class MyViewHolder(var binding: ItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init{
            binding.root.setOnClickListener{    // when anywhere inside item is clicked, then this is shown
                onItemClick?.invoke(adapterPosition)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding = ItemViewBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.typeView.text = dataList.get(position).viewType
    }
}