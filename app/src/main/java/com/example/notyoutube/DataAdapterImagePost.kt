package com.example.notyoutube

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notyoutube.databinding.ItemViewImagePostCommunityProfileBinding
import com.squareup.picasso.Picasso

class DataAdapterImagePost(private var imageList : ArrayList<String>, var context : Context) : RecyclerView.Adapter<DataAdapterImagePost.MyViewHolder>() {
    inner class MyViewHolder(var binding : ItemViewImagePostCommunityProfileBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemViewImagePostCommunityProfileBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Picasso.get().load(imageList[position]).into(holder.binding.imagePost)
        holder.binding.position.text = (position+1).toString()
        holder.binding.totalImages.text = imageList.size.toString()

        holder.binding.imagePost.setOnClickListener{
            val intent = Intent(context, ShowImagePost::class.java)
            intent.putExtra("path", imageList[position])
            context.startActivity(intent)
        }
    }
}