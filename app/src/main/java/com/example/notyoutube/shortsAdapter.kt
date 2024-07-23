package com.example.notyoutube

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.notyoutube.databinding.ItemViewShortsBinding
import com.shashank.sony.fancytoastlib.FancyToast
import render.animations.Bounce
import render.animations.Render
import render.animations.Slide
import render.animations.Zoom

class shortsAdapter(var dataList: ArrayList<shortsDataModel>, var context: Context) :
    RecyclerView.Adapter<shortsAdapter.MyViewHolder>() {
    inner class MyViewHolder(var binding: ItemViewShortsBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding = ItemViewShortsBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.shortsBackgroundVideo.setImageResource(dataList.get(position).shorts_backgroundVideo)
        holder.binding.profileShorts.setImageResource(dataList.get(position).profileShorts)
        holder.binding.songIconShorts.setImageResource(dataList.get(position).songIconShorts)
        holder.binding.ChannelNameShorts.text = dataList.get(position).channel_name
        holder.binding.titleShorts.text = dataList.get(position).title
        holder.binding.songNameShorts.text = dataList.get(position).song_name
        holder.binding.likeCountShorts.text = dataList.get(position).like_count
        holder.binding.commentCountShorts.text = dataList.get(position).comment_count

        holder.binding.likeButtonShorts.setOnClickListener {
            FancyToast.makeText(
                context,
                "Shorts Liked",
                FancyToast.LENGTH_SHORT,
                FancyToast.SUCCESS,
                false
            ).show()
        }

        holder.binding.dislikeButtonShorts.setOnClickListener {
            FancyToast.makeText(
                context,
                "Shorts Disliked",
                FancyToast.LENGTH_SHORT,
                FancyToast.SUCCESS,
                false
            ).show()
        }

        holder.binding.commentButtonShorts.setOnClickListener {
            val activity = context as AppCompatActivity

            val render = Render(context)
            val view = activity.findViewById<FrameLayout>(R.id.frameComment)
            render.setAnimation(Bounce().InUp(view))
            render.start()

            val trans = activity.supportFragmentManager.beginTransaction()
            trans.replace(R.id.frameComment, ShortsCommentFragment(dataList.get(position).comment_count))
            trans.addToBackStack(null)
            trans.commit()
        }

        holder.binding.shareButtonShorts.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "video/*"
            val packageName = "com.example.notyoutube"
            intent.putExtra(Intent.EXTRA_STREAM,Uri.parse("android:resource//" + packageName + "/" + dataList[position].shorts_backgroundVideo))
            context.startActivity(Intent.createChooser(intent, "Share the Video"))
        }

        holder.binding.subscribeButtonShorts.setOnClickListener {
            FancyToast.makeText(
                context,
                "Some Error occurred",
                FancyToast.LENGTH_SHORT,
                FancyToast.ERROR,
                false
            ).show()
        }


        holder.binding.menuShortsButton.setOnClickListener {
            menuShorts().showMenu(context, it)
        }

    }


}