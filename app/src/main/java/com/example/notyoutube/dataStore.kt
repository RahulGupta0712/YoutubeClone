package com.example.notyoutube

class dataStore {
    private lateinit var dataList: ArrayList<dataModel>
    fun getData(): ArrayList<dataModel>{
        dataList =  ArrayList<dataModel>()
        dataList.add(dataModel("Home", false))
        dataList.add(dataModel("Videos", false))
        dataList.add(dataModel("Shorts", false))
        dataList.add(dataModel("Live", false))
        dataList.add(dataModel("Playlists", false))
        dataList.add(dataModel("Community", false))

        return dataList
    }
}