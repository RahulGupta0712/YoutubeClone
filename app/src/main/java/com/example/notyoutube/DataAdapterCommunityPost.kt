package com.example.notyoutube

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.notyoutube.databinding.ItemViewCommunityPostProfileBinding
import com.shashank.sony.fancytoastlib.FancyToast

class DataAdapterCommunityPost(var context : Context, var datalist : ArrayList<DataModelCommunityPost>) : RecyclerView.Adapter<DataAdapterCommunityPost.MyViewHolder>() {
    inner class MyViewHolder(var binding:ItemViewCommunityPostProfileBinding) : RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemViewCommunityPostProfileBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.profileChannelCommunityPost.setImageResource(datalist[position].profile)

        val imageSource = datalist[position].imagePost
        if (imageSource != null)
            holder.binding.imagePostCommunityPost.setImageResource(imageSource)
        else
            holder.binding.imagePostCommunityPost.setImageDrawable(null)

        holder.binding.channelNameCommunityPost.text = datalist[position].channelName
        holder.binding.textPostCommunityPost.text = datalist[position].textPost
        holder.binding.timeAgoCommunityPost.text = datalist[position].timeAgo
        holder.binding.likeCountCommunityPost.text = datalist[position].likeCount
        holder.binding.commentCountCommunityPost.text = datalist[position].commentCount

        holder.binding.likeButtonCommunityPost.setOnClickListener {
            FancyToast.makeText(context as AppCompatActivity, "Post Liked", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
        }

        holder.binding.dislikeButtonCommunityPost.setOnClickListener {
            FancyToast.makeText(context as AppCompatActivity, "Post disliked", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show()
        }

        holder.binding.commentButtonCommunityPost.setOnClickListener {
            FancyToast.makeText(context as AppCompatActivity, "This Feature is coming soon...", FancyToast.LENGTH_LONG, FancyToast.DEFAULT, false).show()
        }

        holder.binding.shareButtonCommunityPost.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/*"
            intent.putExtra(Intent.EXTRA_TEXT, datalist[position].textPost)
            val activity = context as AppCompatActivity
            activity.startActivity(Intent.createChooser(intent, ""))
        }
    }
}