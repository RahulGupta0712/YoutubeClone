package com.example.notyoutube

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notyoutube.databinding.ShortsItemViewSubsBinding
import com.squareup.picasso.Picasso

class DataAdapterShortsSubs(var datalist : ArrayList<DataModelVideoDetails>, var context : Context) : RecyclerView.Adapter<DataAdapterShortsSubs.MyViewHolder>() {
    inner class MyViewHolder(var binding : ShortsItemViewSubsBinding):RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ShortsItemViewSubsBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.titleShortsSubs.text = datalist[position].title

        Picasso.get().load(datalist[position].profileUrl).into(holder.binding.profileChannelSubs)
        Picasso.get().load(datalist[position].thumbnailUrl).into(holder.binding.thumbnailShortsSubs)

        holder.binding.root.setOnClickListener{
            val intent = Intent(context, ItemViewShorts::class.java)
            intent.putExtra("data", datalist[position])
            context.startActivity(intent)
        }
    }
}