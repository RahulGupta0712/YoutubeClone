package com.example.notyoutube

class DataListPlaylist {
    private lateinit var datalist:ArrayList<DataModelPlaylists>
    fun getData():ArrayList<DataModelPlaylists>{
        datalist = ArrayList<DataModelPlaylists>()
        datalist.add(DataModelPlaylists(R.drawable.nature, "The Nature", "Public", "10"))
        datalist.add(DataModelPlaylists(R.drawable.cover2, "Memes 2024", "Unlisted", "83"))
        datalist.add(DataModelPlaylists(R.drawable.short3, "T20 WC 24", "Public", "24"))
        datalist.add(DataModelPlaylists(R.drawable.shorts19, "T20 WC 24 Celebration in India", "Private", "9"))
        datalist.add(DataModelPlaylists(R.drawable.shorts28, "Virat Kohli's Centuries", "Public", "80"))
        datalist.add(DataModelPlaylists(R.drawable.shorts36, "CID Most Funny Scenes", "Public", "13"))
        return datalist
    }
}