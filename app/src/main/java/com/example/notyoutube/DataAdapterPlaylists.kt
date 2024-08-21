package com.example.notyoutube

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notyoutube.databinding.ItemViewPlaylistsBinding

class DataAdapterPlaylists(var context : Context, var datalist:ArrayList<DataModelPlaylists>) : RecyclerView.Adapter<DataAdapterPlaylists.MyViewHolder>() {
    inner class MyViewHolder(var binding : ItemViewPlaylistsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemViewPlaylistsBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.thumbnailPlaylist.setImageResource(datalist[position].thumbnail)
        holder.binding.titlePlaylist.text = datalist[position].title
        holder.binding.visibilityPlaylist.text = datalist[position].visibility
        holder.binding.videoCountPlaylist.text = datalist[position].videoCount
    }


}