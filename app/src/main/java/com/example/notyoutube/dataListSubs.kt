package com.example.notyoutube

class dataListSubs {
    private lateinit var datalist : ArrayList<dataModelSubs>

    fun getData():ArrayList<dataModelSubs>{
        datalist = ArrayList<dataModelSubs>()
        datalist.add(dataModelSubs(R.drawable.vk1, "Virat Kohli"))
        datalist.add(dataModelSubs(R.drawable.narendra_modi, "Narendra Modi"))
        datalist.add(dataModelSubs(R.drawable.pat_cummins, "Pat Cummins"))
        datalist.add(dataModelSubs(R.drawable.salman_khan, "Salman Khan"))
        datalist.add(dataModelSubs(R.drawable.user_avatar_2, "Dhruv Rathee"))
        datalist.add(dataModelSubs(R.drawable.shorts35, "ACP Pradyuman"))
        datalist.add(dataModelSubs(R.drawable.shorts32, "Abhijeet CID"))
        datalist.add(dataModelSubs(R.drawable.vk1, "Virat Kohli"))
        datalist.add(dataModelSubs(R.drawable.narendra_modi, "Narendra Modi"))
        datalist.add(dataModelSubs(R.drawable.pat_cummins, "Pat Cummins"))
        datalist.add(dataModelSubs(R.drawable.salman_khan, "Salman Khan"))
        datalist.add(dataModelSubs(R.drawable.user_avatar_2, "Dhruv Rathee"))
        datalist.add(dataModelSubs(R.drawable.shorts35, "ACP Pradyuman"))
        datalist.add(dataModelSubs(R.drawable.shorts32, "Abhijeet CID"))
        datalist.add(dataModelSubs(R.drawable.vk1, "Virat Kohli"))
        datalist.add(dataModelSubs(R.drawable.narendra_modi, "Narendra Modi"))
        datalist.add(dataModelSubs(R.drawable.pat_cummins, "Pat Cummins"))
        datalist.add(dataModelSubs(R.drawable.salman_khan, "Salman Khan"))
        datalist.add(dataModelSubs(R.drawable.user_avatar_2, "Dhruv Rathee"))
        datalist.add(dataModelSubs(R.drawable.shorts35, "ACP Pradyuman"))
        datalist.add(dataModelSubs(R.drawable.shorts32, "Abhijeet CID"))

        datalist.shuffle()

        return datalist
    }
}