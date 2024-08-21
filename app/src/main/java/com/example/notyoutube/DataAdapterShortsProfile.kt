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
import com.example.notyoutube.databinding.ItemViewProfileHomeShortsBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.firestore
import com.shashank.sony.fancytoastlib.FancyToast
import com.squareup.picasso.Picasso

class DataAdapterShortsProfile(
    var context: Context,
    var datalist: ArrayList<DataModelVideoDetails>
) : RecyclerView.Adapter<DataAdapterShortsProfile.MyViewHolder>() {
    inner class MyViewHolder(var binding: ItemViewProfileHomeShortsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemViewProfileHomeShortsBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return datalist.size
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Picasso.get().load(datalist[position].thumbnailUrl).into(holder.binding.thumbnailShortsHomeProfile)
        holder.binding.profileHomeViewCount.text = context.getString(R.string.zero)
        holder.binding.root.setOnClickListener {
            val intent = Intent(context as AppCompatActivity, ItemViewShorts::class.java)
            intent.putExtra("data", datalist[position])
            (context as AppCompatActivity).startActivity(intent)
        }

        // inflate menu
        holder.binding.menuButtonShortsProfile.setOnClickListener {
            val popup = PopupMenu(context, it)
            popup.menuInflater.inflate(R.menu.menu_shorts_profile, popup.menu)
            popup.show()

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.EditShorts -> {
                        val edit = EditVideoShortsBinding.inflate(LayoutInflater.from(context as AppCompatActivity))
                        edit.newTitle.setText(datalist[position].title)
                        edit.newDescription.setText(datalist[position].description)

                        SweetAlertDialog(context as AppCompatActivity, SweetAlertDialog.NORMAL_TYPE)
                            .setTitleText("Edit Shorts")
                            .setContentText("You can only edit your title and description, if you want to change thumbnail or shorts, then delete the post and re-upload")
                            .setCustomView(edit.root)
                            .setConfirmButton("Save"){ dia ->
                                val new_title = edit.newTitle.text.toString()
                                val new_description = edit.newDescription.text.toString()
                                updateDatabase(datalist[position].key, new_title, new_description)
                                FancyToast.makeText(context, "Shorts Updated", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
                                dia.dismiss()
                            }
                            .setCancelButton("Cancel"){dia ->
                                dia.dismiss()
                            }
                            .show()

                        true
                    }

                    R.id.DownloadShorts -> {
                        Toast.makeText(context, "Downloading Shorts....", Toast.LENGTH_SHORT).show()
                        true
                    }

                    R.id.deleteShort -> {
                        SweetAlertDialog(context as AppCompatActivity, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Delete Shorts")
                            .setConfirmButton("YES"){ dia ->
                                val auth = FirebaseAuth.getInstance()
                                val databaseRef = FirebaseDatabase.getInstance().reference
                                val user = auth.currentUser
                                user?.let {
                                    databaseRef.child("users").child(user.uid).child("Shorts").child(datalist[position].key).removeValue()
                                    val id = datalist[position].videoId
                                    Log.d("new", "id : $id")
                                    if(id.isNotEmpty()) {
                                        val db = Firebase.firestore
                                        db.collection("Shorts").document(id).delete()
                                            .addOnSuccessListener {
                                                FancyToast.makeText(
                                                    context,
                                                    "Shorts Deleted",
                                                    FancyToast.LENGTH_SHORT,
                                                    FancyToast.SUCCESS,
                                                    false
                                                ).show()
                                            }
                                            .addOnFailureListener {
                                                FancyToast.makeText(
                                                    context,
                                                    "Shorts Deletion Failed",
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

                    R.id.shareShorts -> {
                        Toast.makeText(context, "Sharing Shorts....", Toast.LENGTH_SHORT).show()
                        true
                    }

                    else -> false
                }
            }
        }


    }

    private fun updateDatabase(key : String, newTitle:String, newDesc : String){
        val auth = FirebaseAuth.getInstance()
        val databaseRef = FirebaseDatabase.getInstance().reference

        val user = auth.currentUser
        user?.let {
            databaseRef.child("users").child(user.uid).child("Shorts").child(key).child("title").setValue(newTitle)
            databaseRef.child("users").child(user.uid).child("Shorts").child(key).child("description").setValue(newDesc)
        }
    }
}