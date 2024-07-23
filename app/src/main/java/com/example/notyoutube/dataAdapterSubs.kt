package com.example.notyoutube

import android.content.Context
import android.net.Uri
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.notyoutube.databinding.ItemViewSubsChannelBinding
import kotlinx.coroutines.selects.select
import java.util.logging.Handler

class dataAdapterSubs(var datalist: ArrayList<dataModelSubs>, var context: Context) :
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
    var selectedPosition = -1;
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.profileChannelSubs.setImageResource(datalist[position].profile)
        holder.binding.channelNameSubs.text = datalist[position].channelName

        holder.binding.root.setOnClickListener {
            selectedPosition = position
            val activity = context as AppCompatActivity
            val trans = activity.supportFragmentManager.beginTransaction()
            trans.replace(R.id.frame_feed_subs,subsChannelFragment(datalist[position].channelName, datalist[position].profile))
            trans.addToBackStack(null)
            trans.commit()

            notifyDataSetChanged()  // game changer ---- it tells recycler view that the dataset has changed, so it again creates whole list, Kind of Refresh
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