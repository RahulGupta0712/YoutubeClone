package com.example.notyoutube

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.notyoutube.databinding.ItemViewProfileHomeShortsBinding

class DataAdapterShortsProfile(var context : Context, var datalist:ArrayList<DataModelShortsProfile>) : RecyclerView.Adapter<DataAdapterShortsProfile.MyViewHolder>() {
    inner class MyViewHolder(var binding : ItemViewProfileHomeShortsBinding) : RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemViewProfileHomeShortsBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    var onClick : ((Int) -> Unit)? = null

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.thumbnailShortsHomeProfile.setImageResource(datalist[position].thumbnail)
        holder.binding.profileHomeViewCount.text = datalist[position].viewCount
        holder.binding.root.setOnClickListener{
            val trans = (context as AppCompatActivity).supportFragmentManager.beginTransaction()

            var dataList = shortsDataList().getData()
            val n = dataList.size

            // finding the shorts which is clicked, in datalist and then we start the shorts fragment and start from this shorts only, also we are changing the channel Name and profile picture of channel
            for(i in 0..<n){
                dataList[i].channel_name = "The Memes"
                dataList[i].profileShorts = R.drawable.emoji
                if(dataList[i].shorts_backgroundVideo == datalist[position].thumbnail){
                    val temp = dataList[0]
                    dataList[0] = dataList[i]
                    dataList[i] = temp
                }
            }

            trans.add(R.id.frameProfile, FragmentShorts(dataList))
            trans.addToBackStack(null)
            trans.commit()
        }
    }
}