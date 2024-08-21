package com.example.notyoutube

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.notyoutube.databinding.ItemViewShortsCommentBinding
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class dataAdapterCommentShorts (var dataList : ArrayList<dataModelCommentShorts>, var context:Context) :RecyclerView.Adapter<dataAdapterCommentShorts.MyViewHolder>() {
    inner class MyViewHolder(var binding : ItemViewShortsCommentBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemViewShortsCommentBinding.inflate(LayoutInflater.from(context),parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.profileShortsComment.setImageResource(dataList[position].profile)
        holder.binding.channelNameShortsComment.text = dataList[position].channelName
        holder.binding.timeAgoCommentShorts.text = dataList[position].timeAgo
        holder.binding.commentCommentShorts.text = dataList[position].comment
        holder.binding.replyCountCommentShorts.text = dataList[position].replyCount

        holder.binding.likeButtonCommentShorts.setOnClickListener{
            MotionToast.darkColorToast(context as AppCompatActivity,"Shorts", "Comment liked", MotionToastStyle.SUCCESS, MotionToast.GRAVITY_TOP, MotionToast.SHORT_DURATION, ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular))
        }

        holder.binding.dislikeButtonCommentShorts.setOnClickListener{
            MotionToast.createColorToast(context as AppCompatActivity,"Shorts", "Comment disliked", MotionToastStyle.SUCCESS, MotionToast.GRAVITY_CENTER, MotionToast.SHORT_DURATION, ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular))
        }

        holder.binding.commentButtonCommentShorts.setOnClickListener{
            MotionToast.darkToast(context as AppCompatActivity,"Shorts", "This feature is coming soon...", MotionToastStyle.INFO, MotionToast.GRAVITY_BOTTOM, MotionToast.SHORT_DURATION, ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular))
        }

        holder.binding.profileShortsComment.setOnClickListener{
            MotionToast.darkToast(context as AppCompatActivity,"Shorts", "Internet is very slow...", MotionToastStyle.NO_INTERNET, MotionToast.GRAVITY_BOTTOM, MotionToast.SHORT_DURATION, ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular))
        }

        holder.binding.menuButtonCommentShorts.setOnClickListener{
            MotionToast.darkColorToast(context as AppCompatActivity,"Shorts", "Some Error occurred", MotionToastStyle.ERROR, MotionToast.GRAVITY_BOTTOM, MotionToast.SHORT_DURATION, ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular))
        }

        // both the below will do same
        holder.binding.replyCountCommentShorts.setOnClickListener{
            MotionToast.darkToast(context as AppCompatActivity,"Shorts", "This feature is coming soon...", MotionToastStyle.WARNING, MotionToast.GRAVITY_BOTTOM, MotionToast.SHORT_DURATION, ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular))
        }
        holder.binding.textView22.setOnClickListener{
            MotionToast.darkToast(context as AppCompatActivity,"Shorts", "This feature is coming soon...", MotionToastStyle.WARNING, MotionToast.GRAVITY_BOTTOM, MotionToast.SHORT_DURATION, ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular))
        }
    }
}