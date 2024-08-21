package com.example.notyoutube

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notyoutube.databinding.ActivitySinglePostCommunityPostProfileBinding
import com.shashank.sony.fancytoastlib.FancyToast
import com.squareup.picasso.Picasso
import kotlin.math.max

class DataAdapterCommunity2(var datalist: ArrayList<CommunityPostInfo>, var profile:String, var channelName: String, var context: Context, private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<DataAdapterCommunity2.MyViewHolder>() {
    inner class MyViewHolder(var binding: ActivitySinglePostCommunityPostProfileBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface OnItemClickListener{
        fun onEditClick(postKey :String, postText:String, imagePost : ArrayList<String>, postTime:String)
        fun onDeleteClick(postKey : String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ActivitySinglePostCommunityPostProfileBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Picasso.get().load(profile).into(holder.binding.profileChannelCommunityPost)

        holder.binding.channelNameCommunityPost.text = channelName
        holder.binding.textPostCommunityPost.text = datalist[position].textPost
        holder.binding.timeAgoCommunityPost.text = datalist[position].postTime
        holder.binding.likeCountCommunityPost.text = context.getString(R.string.zero)
        holder.binding.commentCountCommunityPost.text = context.getString(R.string.zero)

        holder.binding.likeButtonCommunityPost.setOnClickListener {
            var likes = holder.binding.likeCountCommunityPost.text.toString().toInt()
            likes++
            holder.binding.likeCountCommunityPost.text = likes.toString()
            FancyToast.makeText(
                context as AppCompatActivity,
                "Post Liked",
                FancyToast.LENGTH_SHORT,
                FancyToast.SUCCESS,
                false
            ).show()
        }
        holder.binding.dislikeButtonCommunityPost.setOnClickListener {
            var likes = holder.binding.likeCountCommunityPost.text.toString().toInt()
            likes--
            likes = max(likes, 0)
            holder.binding.likeCountCommunityPost.text = likes.toString()
            FancyToast.makeText(
                context as AppCompatActivity,
                "Post disliked",
                FancyToast.LENGTH_SHORT,
                FancyToast.INFO,
                false
            ).show()
        }
        holder.binding.commentButtonCommunityPost.setOnClickListener {
            FancyToast.makeText(
                context as AppCompatActivity,
                "This Feature is coming soon...",
                FancyToast.LENGTH_LONG,
                FancyToast.DEFAULT,
                false
            ).show()
        }
        holder.binding.shareButtonCommunityPost.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/*"
            intent.putExtra(Intent.EXTRA_TEXT, datalist[position].textPost)
            val activity = context as AppCompatActivity
            activity.startActivity(Intent.createChooser(intent, ""))
        }
        holder.binding.menuButtonCommunityPost.setOnClickListener{
            val popup = PopupMenu(context,holder.binding.menuButtonCommunityPost)
            popup.menuInflater.inflate(R.menu.menu_community_post, popup.menu)
            popup.show()

            popup.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.editPost -> {
                        itemClickListener.onEditClick(datalist[position].postKey,datalist[position].textPost,datalist[position].imageList, datalist[position].postTime)
                        true
                    }

                    R.id.deletePost -> {
                        itemClickListener.onDeleteClick(datalist[position].postKey)
                        true
                    }

                    else -> false
                }
            }
        }

        // show image posts in horizontal recycler view
        holder.binding.rvImagePosts.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)
        val Adapter = DataAdapterImagePost(datalist[position].imageList, context as AppCompatActivity)
        holder.binding.rvImagePosts.adapter = Adapter

        val snapHelper = PagerSnapHelper()
        val currentSnapHelper = holder.binding.rvImagePosts.onFlingListener as? PagerSnapHelper
        if(currentSnapHelper == null) snapHelper.attachToRecyclerView(holder.binding.rvImagePosts)
    }
}