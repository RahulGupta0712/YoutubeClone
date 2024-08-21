package com.example.notyoutube

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.notyoutube.databinding.SingleVideoHomeBinding
import com.squareup.picasso.Picasso

class adapter_home_video(var dataList: ArrayList<DataModelVideoDetails>, var context: Context) :
    RecyclerView.Adapter<adapter_home_video.MyViewHolder>() {
    private val ytMenu = YoutubeMenu()

    inner class MyViewHolder(var binding: SingleVideoHomeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = SingleVideoHomeBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        animation(holder.binding.root)

        Picasso.get().load(dataList[position].thumbnailUrl).into(holder.binding.thumbnail)
        Picasso.get().load(dataList[position].profileUrl).into(holder.binding.profile)

        val time = (System.currentTimeMillis() - dataList[position].timePosted) / 1000
        val minutes = time / 60
        val hour = minutes / 60
        val days = hour / 24
        val month = days / 30
        val year = days / 365

        val show =
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

        val h = adjust(hr)
        val m = adjust(min)
        val s = adjust(sec)

        holder.binding.time.text =
            if (h == "00") {
                "$m:$s"
            } else {
                "$h:$m:$s"
            }


        holder.binding.title.text = dataList[position].title
        holder.binding.channelName.text = dataList[position].channelName
        holder.binding.countViews.text = context.getString(R.string.zero)


        holder.binding.menuButton.setOnClickListener {
            ytMenu.customMenu(context, it)
        }

        holder.binding.thumbnail.setOnClickListener {
            val intent = Intent(context, videoFullModeProfile::class.java)
            intent.putExtra("video", dataList[position])
            context.startActivity(intent)
        }

        holder.binding.root.setOnClickListener {
            Toast.makeText(
                context,
                "Showing the video: " + dataList[position].title,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun animation(view: View) {
        val anim = AlphaAnimation(0.0f, 1.0f)   // from 0 % opacity to 100 % opacity
        anim.duration = 1000
        view.startAnimation(anim)
    }

    private fun adjust(time: Long): String {
        return when (time) {
            0L -> "00" // 0 digit
            in 1..9 -> "0$time" // 1 digit
            else -> "$time" //  2 digit
        }
    }
}