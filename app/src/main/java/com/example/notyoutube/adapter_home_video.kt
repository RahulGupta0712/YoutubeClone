package com.example.notyoutube

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.notyoutube.databinding.SingleVideoHomeBinding
import com.squareup.picasso.Picasso

class adapter_home_video(var dataList: ArrayList<DataModelVideoDetails>, var context: Context) :
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

        Picasso.get().load(dataList.get(position).thumbnailUrl).into(holder.binding.thumbnail)
        Picasso.get().load(dataList.get(position).profileUrl).into(holder.binding.profile)

        val time = (System.currentTimeMillis()-dataList[position].timePosted)/1000
        val minutes = time / 60
        val hour = minutes / 60
        val days = hour / 24
        val month = days / 30
        val year = days / 365

        val show=
            if (year > 0) "$year year(s)"
            else if (month > 0) "$month month(s)"
            else if (days > 0) "$days day(s)"
            else if (hour > 0) "$hour hr"
            else if (minutes > 0) "$minutes min"
            else "$time sec"

        holder.binding.timeAgo.text = show

        var sec = dataList[position].videoLength
        val hr = sec / 3600
        sec %= 3600
        val min = sec / 60
        sec %= 60

        var h = adjust(hr)
        var m = adjust(min)
        var s = adjust(sec)

        holder.binding.time.text =
            if(h == "00"){
                "$m:$s"
            }
            else{
                "$h:$m:$s"
            }


        holder.binding.title.text = dataList.get(position).title
        holder.binding.channelName.text = dataList.get(position).channelName
        holder.binding.countViews.text = "0"


        holder.binding.menuButton.setOnClickListener{
            ytMenu.customMenu(context, it)
        }

        holder.binding.thumbnail.setOnClickListener{
            val intent = Intent(context, videoFullModeProfile::class.java)
            intent.putExtra("video", dataList[position])
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
    private fun adjust(time : Long):String{
        if(time == 0L){
            // 0 digit
            return "00"
        }
        else if(time > 0L && time < 10L){
            // 1 digit
            return "0$time"
        }
        else{
            // > 1 digit
            return "$time"
        }
    }
}