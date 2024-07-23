package com.example.notyoutube

class DataListShortsFragment {
    private lateinit var datalist : ArrayList<DataModelShortsProfile>
    fun getData() : ArrayList<DataModelShortsProfile>{
        datalist = ArrayList<DataModelShortsProfile>()
        datalist.add(DataModelShortsProfile(R.drawable.shorts35, "100M"))
        datalist.add(DataModelShortsProfile(R.drawable.shorts1, "100K"))
        datalist.add(DataModelShortsProfile(R.drawable.shorts2, "9M"))
        datalist.add(DataModelShortsProfile(R.drawable.shorts4, "10K"))
        datalist.add(DataModelShortsProfile(R.drawable.shorts5, "32K"))
        datalist.add(DataModelShortsProfile(R.drawable.shorts6, "10K"))
        datalist.add(DataModelShortsProfile(R.drawable.shorts7, "27"))
        datalist.add(DataModelShortsProfile(R.drawable.shorts8, "181"))
        datalist.add(DataModelShortsProfile(R.drawable.shorts9, "0"))
        datalist.add(DataModelShortsProfile(R.drawable.shorts10, "3M"))
        datalist.add(DataModelShortsProfile(R.drawable.shorts12, "100M"))
        datalist.add(DataModelShortsProfile(R.drawable.shorts13, "100K"))
        datalist.add(DataModelShortsProfile(R.drawable.shorts14, "9M"))
        datalist.add(DataModelShortsProfile(R.drawable.shorts15, "10K"))
        datalist.add(DataModelShortsProfile(R.drawable.shorts16, "32K"))
        datalist.add(DataModelShortsProfile(R.drawable.shorts17, "10K"))
        datalist.add(DataModelShortsProfile(R.drawable.shorts18, "27"))
        datalist.add(DataModelShortsProfile(R.drawable.shorts19, "181"))
        datalist.add(DataModelShortsProfile(R.drawable.shorts20, "0"))
        datalist.add(DataModelShortsProfile(R.drawable.shorts21, "3M"))
        return datalist
    }
}