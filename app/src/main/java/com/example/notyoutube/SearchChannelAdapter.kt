package com.example.notyoutube

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.notyoutube.databinding.ItemViewSearchResultChannelBinding
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.channelFlow

class SearchChannelAdapter(var context: Context, var datalist: ArrayList<String>) :
    RecyclerView.Adapter<SearchChannelAdapter.MyViewHolder>() {
    inner class MyViewHolder(var binding: ItemViewSearchResultChannelBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemViewSearchResultChannelBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val db = FirebaseDatabase.getInstance().reference
        var channelName: String = ""
        var username: String = ""
        var profileUrl: String = ""
        db.child("users").child(datalist[position]).child("Channel Name").get()
            .addOnSuccessListener {
                channelName = it.value.toString()
                holder.binding.channelName.text = channelName
            }
        db.child("users").child(datalist[position]).child("Username").get()
            .addOnSuccessListener {
                username = "@${it.value.toString()}"
                holder.binding.username.text = username
            }
        db.child("users").child(datalist[position]).child("Profile Picture").get()
            .addOnSuccessListener {
                profileUrl = it.value.toString()
                Picasso.get().load(profileUrl).into(holder.binding.profile)
            }


        holder.binding.root.setOnClickListener {
            val intent = Intent(context, Profile::class.java)
            intent.putExtra("channelId", datalist[position])
            context.startActivity(intent)
        }
    }


}