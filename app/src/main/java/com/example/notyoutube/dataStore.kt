package com.example.notyoutube

class dataStore {
    private lateinit var dataList: ArrayList<dataModel>
    fun getData(): ArrayList<dataModel>{
        dataList =  ArrayList<dataModel>()
        dataList.add(dataModel("Home"))
        dataList.add(dataModel("Videos"))
        dataList.add(dataModel("Shorts"))
        dataList.add(dataModel("Live"))
        dataList.add(dataModel("Playlists"))
        dataList.add(dataModel("Community"))

        return dataList
    }
}