package com.example.notyoutube

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.notyoutube.databinding.EditVideoShortsBinding
import com.example.notyoutube.databinding.ProfileVideosBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.firestore
import com.shashank.sony.fancytoastlib.FancyToast
import com.squareup.picasso.Picasso

class MyVideoAdapter(private var dataList : ArrayList<DataModelVideoDetails>, var context : Context) : RecyclerView.Adapter<MyVideoAdapter.MyViewHolder>() {
    inner class MyViewHolder(var binding:ProfileVideosBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ProfileVideosBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Picasso.get().load(dataList[position].thumbnailUrl).into(holder.binding.thumbnail1)
        holder.binding.title1.text = dataList[position].title
        holder.binding.viewCount1.text = context.getString(R.string.zero)

        holder.binding.showVisibility.setImageResource(if(dataList[position].visibility == "Public") R.drawable.intenet_network else R.drawable.password)

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

        val h = adjust(hr)
        val m = adjust(min)
        val s = adjust(sec)

        holder.binding.time1.text =
            if(h == "00"){
                "$m:$s"
            }
        else{
                "$h:$m:$s"
            }

        holder.binding.likeCount.text = context.getString(R.string.zero)
        holder.binding.commentCount.text = context.getString(R.string.zero)
        holder.binding.stream.text = context.getString(R.string.empty)

        holder.binding.root.setOnClickListener{
            val intent = Intent(context, videoFullModeProfile::class.java)
            intent.putExtra("video", dataList[position])
            context.startActivity(intent)
        }
        holder.binding.title1.setOnClickListener{
            Toast.makeText(context,dataList[position].title, Toast.LENGTH_SHORT).show()
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
                            .setConfirmButton("Save"){dia ->
                                val new_title = edit.newTitle.text.toString()
                                val new_description = edit.newDescription.text.toString()
                                updateDatabase(dataList[position].key, new_title, new_description)
                                FancyToast.makeText(context, "Video Updated", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
                                dia.dismiss()
                            }
                            .setCancelButton("Cancel"){dia ->
                                dia.dismiss()
                            }
                            .show()

                        true
                    }
                    R.id.delete -> {
                        SweetAlertDialog(context as AppCompatActivity, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Delete Video")
                            .setConfirmButton("YES"){dia ->
                                val auth = FirebaseAuth.getInstance()
                                val databaseRef = FirebaseDatabase.getInstance().reference
                                val user = auth.currentUser
                                user?.let {
                                    databaseRef.child("users").child(user.uid).child("Videos").child(dataList[position].key).removeValue()
                                    val id = dataList[position].videoId
                                    Log.d("new", "id : ${dataList[position].videoId}")
                                    if(id.isNotEmpty()) {
                                        val db = Firebase.firestore
                                        db.collection("Videos").document(id).delete()
                                            .addOnSuccessListener {
                                                FancyToast.makeText(
                                                    context,
                                                    "Video Deleted",
                                                    FancyToast.LENGTH_SHORT,
                                                    FancyToast.SUCCESS,
                                                    false
                                                ).show()
                                            }
                                            .addOnFailureListener {
                                                FancyToast.makeText(
                                                    context,
                                                    "Video Deletion Failed",
                                                    FancyToast.LENGTH_SHORT,
                                                    FancyToast.ERROR,
                                                    false
                                                ).show()
                                            }
                                    }
                                }

                                dia.dismiss()
                            }
                            .setCancelButton("NO"){dia ->
                                dia.dismiss()
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
        return when(time){
            0L -> "00"
            in 1..9 -> "0$time"
            else -> "$time"
        }
    }


}