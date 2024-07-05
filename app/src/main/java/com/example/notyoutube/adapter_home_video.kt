package com.example.notyoutube

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.notyoutube.databinding.SingleVideoHomeBinding

class adapter_home_video(var dataList: ArrayList<data_model_home_video>, var context: Context) :
    RecyclerView.Adapter<adapter_home_video.MyViewHolder>() {
    val ytMenu = YoutubeMenu()
    inner class MyViewHolder(var binding: SingleVideoHomeBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding = SingleVideoHomeBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        animation(holder.binding.root)

        holder.binding.thumbnail.setImageResource(dataList.get(position).thumbnail)
        holder.binding.profile.setImageResource(dataList.get(position).profile)
        holder.binding.time.text = dataList.get(position).time
        holder.binding.title.text = dataList.get(position).title
        holder.binding.channelName.text = dataList.get(position).channelName
        holder.binding.countViews.text = dataList.get(position).viewsCount
        holder.binding.timeAgo.text = dataList.get(position).timeAgo

        holder.binding.menuButton.setOnClickListener{
            ytMenu.customMenu(context, it)
        }

        holder.binding.thumbnail.setOnClickListener{
            val intent = Intent(context, videoFullMode::class.java)
            intent.putExtra("", position)
            context.startActivity(intent)
        }

        holder.binding.root.setOnClickListener {
            Toast.makeText(context,"Showing the video: " + dataList.get(position).title, Toast.LENGTH_SHORT).show()
        }
    }

    fun animation(view : View){
        val anim = AlphaAnimation(0.0f, 1.0f)   // from 0 % opacity to 100 % opacity
        anim.duration = 1000

        view.startAnimation(anim)
    }

}