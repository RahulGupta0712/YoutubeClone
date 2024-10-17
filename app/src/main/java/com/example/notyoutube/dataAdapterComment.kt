package com.example.notyoutube

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.notyoutube.databinding.ItemViewShortsCommentBinding
import com.github.ybq.android.spinkit.style.WanderingCubes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.squareup.picasso.Picasso
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class dataAdapterComment(var dataList: ArrayList<CommentModel>, var context: Context) :
    RecyclerView.Adapter<dataAdapterComment.MyViewHolder>() {
    inner class MyViewHolder(var binding: ItemViewShortsCommentBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemViewShortsCommentBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        holder.binding.group3.isVisible = false

        holder.binding.progressBar10.indeterminateDrawable = WanderingCubes()
        holder.binding.progressBar10.isVisible = true


        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference;

        val auth = FirebaseAuth.getInstance()

        val userId = dataList[position].userId;
        var channelName: String
        var profileUrl: String

        val db = databaseReference.child("users").child(userId)
        // we will fetch the comment only if this user exists now in the database, if user does not exist, then don't fetch as it leads to app crash

        db.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // user exists
                    channelName = dataSnapshot.child("Channel Name").getValue(String::class.java)?:"xyz"
                    profileUrl = dataSnapshot.child("Profile Picture").getValue(String::class.java)?:""

                    if(profileUrl.isNotEmpty()) {
                        Picasso.get().load(profileUrl)
                            .into(holder.binding.profileShortsComment)
                    }

                    holder.binding.channelNameShortsComment.text = channelName

                    val seconds =
                        (System.currentTimeMillis() - dataList[position].timePosted) / 1000
                    val minutes = seconds / 60
                    val hour = minutes / 60
                    val days = hour / 24
                    val month = days / 30
                    val year = days / 365

                    val show = if (year > 0) "$year year(s)"
                    else if (month > 0) "$month month(s)"
                    else if (days > 0) "$days day(s)"
                    else if (hour > 0) "$hour hr"
                    else if (minutes > 0) "$minutes min"
                    else "$seconds sec"

                    holder.binding.timeAgoCommentShorts.text = show
                    holder.binding.commentCommentShorts.text =
                        dataList[position].comment
                    holder.binding.replyCountCommentShorts.text =
                        dataList[position].replyCount.toString()

                    holder.binding.group3.isVisible = true
                    holder.binding.progressBar10.isVisible = false
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })


        holder.binding.likeButtonCommentShorts.setOnClickListener {
            dataList[position].likesCount++
            MotionToast.darkColorToast(
                context as AppCompatActivity,
                "Shorts",
                "Comment liked",
                MotionToastStyle.SUCCESS,
                MotionToast.GRAVITY_TOP,
                MotionToast.SHORT_DURATION,
                ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular)
            )
        }

        holder.binding.dislikeButtonCommentShorts.setOnClickListener {
            MotionToast.createColorToast(
                context as AppCompatActivity,
                "Shorts",
                "Comment disliked",
                MotionToastStyle.SUCCESS,
                MotionToast.GRAVITY_CENTER,
                MotionToast.SHORT_DURATION,
                ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular)
            )
        }

        holder.binding.commentButtonCommentShorts.setOnClickListener {
            MotionToast.darkToast(
                context as AppCompatActivity,
                "Shorts",
                "This feature is coming soon...",
                MotionToastStyle.INFO,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.SHORT_DURATION,
                ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular)
            )
        }

        holder.binding.profileShortsComment.setOnClickListener {
            val intent = Intent(context, Profile::class.java)
            intent.putExtra("channelId", dataList[position].userId)
            context.startActivity(intent)
        }
        holder.binding.channelNameShortsComment.setOnClickListener {
            val intent = Intent(context, Profile::class.java)
            intent.putExtra("channelId", dataList[position].userId)
            context.startActivity(intent)
        }

        holder.binding.menuButtonCommentShorts.setOnClickListener {
            MotionToast.darkColorToast(
                context as AppCompatActivity,
                "Shorts",
                "Some Error occurred",
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.SHORT_DURATION,
                ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular)
            )
        }

        // both the below will do same
        holder.binding.replyCountCommentShorts.setOnClickListener {
            MotionToast.darkToast(
                context as AppCompatActivity,
                "Shorts",
                "This feature is coming soon...",
                MotionToastStyle.WARNING,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.SHORT_DURATION,
                ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular)
            )
        }
        holder.binding.textView22.setOnClickListener {
            MotionToast.darkToast(
                context as AppCompatActivity,
                "Shorts",
                "This feature is coming soon...",
                MotionToastStyle.WARNING,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.SHORT_DURATION,
                ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular)
            )
        }
    }
}