package com.example.notyoutube

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.media.MediaPlayer.OnSeekCompleteListener
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.MediaController
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.notyoutube.databinding.ItemViewShortsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shashank.sony.fancytoastlib.FancyToast
import com.squareup.picasso.Picasso
import render.animations.Bounce
import render.animations.Render
import render.animations.Slide
import render.animations.Zoom

class shortsAdapter(var dataList: ArrayList<DataModelVideoDetails>, var context: Context) :
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

    private var currentPos = 0
    override fun onBindViewHolder(holder: MyViewHolder, @SuppressLint("RecyclerView") position: Int) {

        Picasso.get().load(dataList.get(position).thumbnailUrl).into(holder.binding.shortsBackgroundVideo)
        Picasso.get().load(dataList.get(position).profileUrl).into(holder.binding.profileShorts)

        // show video
        holder.binding.shortsView.setVideoURI(Uri.parse(dataList[position].videoUrl))
        holder.binding.shortsView.start()

        holder.binding.shortsView.setOnPreparedListener { mediaPlayer ->
            mediaPlayer?.setOnSeekCompleteListener {
                Log.d("abc", "onSeekComplete")
                holder.binding.shortsView.start()
            }
        }

        holder.binding.shortsView.isVisible = true

        holder.binding.shortsView.setOnInfoListener { _, what, _ ->
            when (what) {
                MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START -> {
                    // Video started rendering
                    holder.binding.shortsBackgroundVideo.isVisible = false
                    holder.binding.progressBar3.isVisible=false
                    Log.d("abc", "video rendering started")
                }
                MediaPlayer.MEDIA_INFO_BUFFERING_START -> {
                    // Buffering started
                    Log.d("abc", "buffering started")
                    holder.binding.progressBar3.isVisible=true
                }
                MediaPlayer.MEDIA_INFO_BUFFERING_END -> {
                    // Buffering ended
                    Log.d("abc", "buffering ended")
                    holder.binding.progressBar3.isVisible=false
                }
            }
            true
        }

        // handling pause play events
        holder.binding.shortsView.setOnClickListener{
            if(holder.binding.shortsView.isPlaying){
                currentPos = holder.binding.shortsView.currentPosition
                holder.binding.shortsView.pause()
                Toast.makeText(context, "Shorts Paused", Toast.LENGTH_SHORT).show()
            }
            else{
                // if video is paused, play it, seekTo is used to go to a specific position, but it is not working here
                Log.d("abc", " before seek")
                holder.binding.shortsView.seekTo(currentPos)
                Log.d("abc", " after seek")
                Toast.makeText(context, "Shorts resumed", Toast.LENGTH_SHORT).show()
            }
        }

        holder.binding.shortsView.setOnCompletionListener {
            holder.binding.shortsView.start()
        }

        holder.binding.songIconShorts.setImageResource(R.drawable.music_button)     // need to make it based on user in future
        holder.binding.ChannelNameShorts.text = dataList.get(position).channelName
        holder.binding.titleShorts.text = dataList.get(position).title
        holder.binding.songNameShorts.text = "Original Audio"   // need to make it based on user in future
        holder.binding.likeCountShorts.text = "0"
        holder.binding.commentCountShorts.text = "0"

        holder.binding.likeButtonShorts.setOnClickListener {
            FancyToast.makeText(
                context,
                "Shorts Liked",
                FancyToast.LENGTH_SHORT,
                FancyToast.SUCCESS,
                false
            ).show()
            val likes = holder.binding.likeCountShorts.text.toString().toInt()
            holder.binding.likeCountShorts.text = (likes+1).toString()
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
            trans.replace(R.id.frameComment, ShortsCommentFragment(holder.binding.commentCountShorts.text.toString()))
            trans.addToBackStack(null)
            trans.commit()
        }

        holder.binding.shareButtonShorts.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "video/*"
            val packageName = "com.example.notyoutube"
            intent.putExtra(Intent.EXTRA_STREAM,Uri.parse(dataList[position].videoUrl))
            context.startActivity(Intent.createChooser(intent, "Share the Video"))
        }


        holder.binding.menuShortsButton.setOnClickListener {
            menuShorts().showMenu(context, it)
        }

        // don't show the subscribe button if already subscribed or shorts is uploaded by same user
        val auth = FirebaseAuth.getInstance()
        val databaseRef = FirebaseDatabase.getInstance().reference
        val user = auth.currentUser

        if(user != null) {
            if (user.uid == dataList[position].channelId) {
                // same channel
               holder.binding.subscribeButtonShorts.isVisible = false
            } else {
                // check if subscribed
                databaseRef.child("users").child(user.uid).child("Subscribed Channels")
                    .addValueEventListener(object :
                        ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (snap in snapshot.children) {
                                val id = snap.getValue(String::class.java)
                                id?.let {
                                    if (id == dataList[position].channelId) {
                                        // subscribed, don't show
                                        holder.binding.subscribeButtonShorts.isVisible = false
                                    }
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }

                    })

            }
        }

        holder.binding.subscribeButtonShorts.setOnClickListener{
            if(user == null){
                // not signed in
                FancyToast.makeText(context, "Sign in to subscribe", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
            }
            else{
                // subscribe
                FancyToast.makeText(context, "Channel Subscribed", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
                databaseRef.child("users").child(user.uid).child("Subscribed Channels").child(dataList[position].channelId).setValue(dataList[position].channelId)
                holder.binding.subscribeButtonShorts.isVisible = false
            }
        }
    }


}