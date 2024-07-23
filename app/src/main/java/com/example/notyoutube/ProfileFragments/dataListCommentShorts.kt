package com.example.notyoutube.ProfileFragments

import com.example.notyoutube.R
import com.example.notyoutube.dataModelCommentShorts

class dataListCommentShorts {
    private lateinit var datalist:ArrayList<dataModelCommentShorts>
    fun getData() : ArrayList<dataModelCommentShorts> {
        datalist = ArrayList<dataModelCommentShorts>()

        datalist.add(dataModelCommentShorts(R.drawable.narendra_modi, "Narendra Modi", "1min", "Highly appreciate the hardwork you put in to get upto this mark. Many many congratulations.", "20M"))
        datalist.add(dataModelCommentShorts(R.drawable.pat_cummins, "Pat Cummins", "3d", "Cheers !!! mate","18K"))
        datalist.add(dataModelCommentShorts(R.drawable.vk1, "Virat Kohli", "16h", "Bapu thaari bowling kamaal chhe", "9M"))
        datalist.add(dataModelCommentShorts(R.drawable.salman_khan, "Salman Khan", "3 mo", "Kamaal hai bhai", "0"))
        datalist.add(dataModelCommentShorts(R.drawable.emoji, "Stuart Big", "6 yr", "Good work", "2"))
        datalist.add(dataModelCommentShorts(R.drawable.user_avatar_2, "private Person", "3w", "congrats on a remarkable experience", "172"))
        datalist.add(dataModelCommentShorts(R.drawable.narendra_modi, "Narendra Modi", "1min", "Highly appreciate the hardwork you put in to get upto this mark. Many many congratulations.", "20M"))
        datalist.add(dataModelCommentShorts(R.drawable.pat_cummins, "Pat Cummins", "3d", "Cheers !!! mate","18K"))
        datalist.add(dataModelCommentShorts(R.drawable.vk1, "Virat Kohli", "16h", "Bapu thaari bowling kamaal chhe", "9M"))
        datalist.add(dataModelCommentShorts(R.drawable.salman_khan, "Salman Khan", "3 mo", "Kamaal hai bhai", "0"))
        datalist.add(dataModelCommentShorts(R.drawable.emoji, "Stuart Big", "6 yr", "Good work", "2"))
        datalist.add(dataModelCommentShorts(R.drawable.user_avatar_2, "private Person", "3w", "congrats on a remarkable experience", "172"))
        datalist.add(dataModelCommentShorts(R.drawable.narendra_modi, "Narendra Modi", "1min", "Highly appreciate the hardwork you put in to get upto this mark. Many many congratulations.", "20M"))
        datalist.add(dataModelCommentShorts(R.drawable.pat_cummins, "Pat Cummins", "3d", "Cheers !!! mate","18K"))
        datalist.add(dataModelCommentShorts(R.drawable.vk1, "Virat Kohli", "16h", "Bapu thaari bowling kamaal chhe", "9M"))
        datalist.add(dataModelCommentShorts(R.drawable.salman_khan, "Salman Khan", "3 mo", "Kamaal hai bhai", "0"))
        datalist.add(dataModelCommentShorts(R.drawable.emoji, "Stuart Big", "6 yr", "Good work", "2"))
        datalist.add(dataModelCommentShorts(R.drawable.user_avatar_2, "private Person", "3w", "congrats on a remarkable experience", "172"))

        datalist.shuffle()
        return datalist
    }
}