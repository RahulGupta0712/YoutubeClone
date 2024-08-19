package com.example.notyoutube

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.notyoutube.databinding.ItemViewSubsChannelBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.coroutines.selects.select
import java.util.logging.Handler

class dataAdapterSubs(var datalist: ArrayList<String>, var context: Context) :
    RecyclerView.Adapter<dataAdapterSubs.MyViewHolder>() {
    inner class MyViewHolder(var binding: ItemViewSubsChannelBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding =
            ItemViewSubsChannelBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference

    var selectedPosition = -1;
    override fun onBindViewHolder(
        holder: MyViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        auth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().reference

        // fetch channel name and profile and show
        var channelName: String? = null
        var profileUrl: String? = null
        databaseRef.child("users").child(datalist[position]).child("Channel Name")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    channelName = snapshot.getValue(String::class.java)
                    channelName?.let {

                        // now fetch profile url
                        databaseRef.child("users").child(datalist[position])
                            .child("Profile Picture")
                            .addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    profileUrl = snapshot.getValue(String::class.java)

                                    // got the channel name and profile url
                                    profileUrl?.let {

                                        // show the name and profile
                                        Picasso.get().load(profileUrl)
                                            .into(holder.binding.profileChannelSubs)
                                        holder.binding.channelNameSubs.text = channelName

                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {

                                }
                            })

                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        holder.binding.root.setOnClickListener {
            if (channelName == null || profileUrl == null) {
                Log.d("abc", " Loading the profile picture and name")
            } else {
                selectedPosition = position
                val activity = context as AppCompatActivity

                val trans = activity.supportFragmentManager.beginTransaction()
                trans.replace(R.id.frame_feed_subs, subsChannelFragment(datalist[position], channelName!!, profileUrl!!))
                trans.commit()

                notifyDataSetChanged()  // game changer ---- it tells recycler view that the dataset has changed, so it again creates whole list, Kind of Refresh
            }
        }


        // if this is the selected item, then do red, else black
        holder.binding.root.setBackgroundResource(
            if (selectedPosition != position) {
                R.color.black
            } else {
                R.color.red
            }
        )
    }
}