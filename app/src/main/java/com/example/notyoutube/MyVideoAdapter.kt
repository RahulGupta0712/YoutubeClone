package com.example.notyoutube

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.notyoutube.databinding.EditVideoShortsBinding
import com.example.notyoutube.databinding.ProfileVideosBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.shashank.sony.fancytoastlib.FancyToast
import com.squareup.picasso.Picasso

class MyVideoAdapter(var dataList : ArrayList<DataModelVideoDetails>, var context : Context) : RecyclerView.Adapter<MyVideoAdapter.MyViewHolder>() {
    inner class MyViewHolder(var binding:ProfileVideosBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding = ProfileVideosBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Picasso.get().load(dataList[position].thumbnailUrl).into(holder.binding.thumbnail1)
        holder.binding.title1.text = dataList.get(position).title
        holder.binding.viewCount1.text = "0"

        val time =
            (System.currentTimeMillis()-dataList[position].timePosted)/1000
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

        holder.binding.timeAgo1.text = show
        var sec = dataList[position].videoLength
        val hr = sec / 3600
        sec %= 3600
        val min = sec / 60
        sec %= 60

        var h = adjust(hr)
        var m = adjust(min)
        var s = adjust(sec)

        holder.binding.time1.text =
            if(h == "00"){
                "$m:$s"
            }
        else{
                "$h:$m:$s"
            }

        holder.binding.likeCount.text = "0"
        holder.binding.commentCount.text = "0"
        holder.binding.stream.text = ""

        holder.binding.root.setOnClickListener{
            var intent = Intent(context, videoFullModeProfile::class.java)
            intent.putExtra("video", dataList[position])
            context.startActivity(intent)
        }
        holder.binding.title1.setOnClickListener{
            Toast.makeText(context,dataList.get(position).title, Toast.LENGTH_SHORT).show()
        }

        holder.binding.menuButtonVideosProfile.setOnClickListener{
            val popup = PopupMenu(context, it)
            popup.menuInflater.inflate(R.menu.menu_profile_videos_single_videos, popup.menu)
            popup.show()

            popup.setOnMenuItemClickListener {item ->
                when(item.itemId){
                    R.id.Edit -> {
                        val edit = EditVideoShortsBinding.inflate(LayoutInflater.from(context as AppCompatActivity))
                        edit.newTitle.setText(dataList[position].title)
                        edit.newDescription.setText(dataList[position].description)

                        SweetAlertDialog(context as AppCompatActivity, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                            .setTitleText("Edit Video")
                            .setContentText("You can only edit your title and description, if you want to change thumbnail or shorts, then delete the post and re-upload")
                            .setCustomView(edit.root)
                            .setConfirmButton("Save"){
                                val new_title = edit.newTitle.text.toString()
                                val new_description = edit.newDescription.text.toString()
                                updateDatabase(dataList[position].key, new_title, new_description)
                                FancyToast.makeText(context, "Video Updated", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
                                it.dismiss()
                            }
                            .setCancelButton("Cancel"){
                                it.dismiss()
                            }
                            .show()

                        true
                    }
                    R.id.delete -> {
                        SweetAlertDialog(context as AppCompatActivity, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Delete Video")
                            .setConfirmButton("YES"){
                                val auth = FirebaseAuth.getInstance()
                                val databaseRef = FirebaseDatabase.getInstance().reference
                                val user = auth.currentUser
                                user?.let {
                                    databaseRef.child("users").child(user.uid).child("Videos").child(dataList[position].key).removeValue()
                                }
                                FancyToast.makeText(context, "Video Deleted", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show()
                                it.dismiss()
                            }
                            .setCancelButton("NO"){
                                it.dismiss()
                            }
                            .show()
                        true
                    }
                    R.id.saveToPlaylist -> {
                        Toast.makeText(context, "Saving the Video to Playlist...", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.share -> {
                        val intent = Intent(Intent.ACTION_SEND)
                        intent.type = "image/*"
//                        intent.putExtra(Intent.EXTRA_STREAM, dataList.get(position).thumbnail)
                        context.startActivity(Intent.createChooser(intent, "Share Video Thumbnail"))
                        true
                    }
                    R.id.share ->{
                        Toast.makeText(context, "Sharing video 🔃", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.downloadVideo -> {
                        Toast.makeText(context, "Downloading the video 📩", Toast.LENGTH_SHORT).show()
                        true
                    }

                    else->false
                }
            }
        }
    }

    private fun updateDatabase(key : String, newTitle:String, newDesc : String){
        val auth = FirebaseAuth.getInstance()
        val databaseRef = FirebaseDatabase.getInstance().reference

        val user = auth.currentUser
        user?.let {
            databaseRef.child("users").child(user.uid).child("Videos").child(key).child("title").setValue(newTitle)
            databaseRef.child("users").child(user.uid).child("Videos").child(key).child("description").setValue(newDesc)
        }
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