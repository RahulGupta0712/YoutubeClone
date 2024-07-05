package com.example.notyoutube

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.notyoutube.databinding.ProfileVideosBinding

class MyVideoAdapter(var dataList : ArrayList<descriptionVideo>, var context : Context) : RecyclerView.Adapter<MyVideoAdapter.MyViewHolder>() {
    inner class MyViewHolder(var binding:ProfileVideosBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding = ProfileVideosBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.thumbnail1.setImageResource(dataList.get(position).thumbnail)
        holder.binding.title1.text = dataList.get(position).title
        holder.binding.viewCount1.text = dataList.get(position).viewCount
        holder.binding.timeAgo1.text = dataList.get(position).timeAgo
        holder.binding.time1.text = dataList.get(position).time
        holder.binding.likeCount.text = dataList.get(position).likeCount
        holder.binding.commentCount.text = dataList.get(position).commentCount

        holder.binding.root.setOnClickListener{
            var intent = Intent(context, videoFullModeProfile::class.java)
            intent.putExtra("", position)
            context.startActivity(intent)
        }
        holder.binding.title1.setOnClickListener{
            Toast.makeText(context,dataList.get(position).title, Toast.LENGTH_SHORT).show()
        }
    }


}