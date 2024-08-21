package com.example.notyoutube

data class CommunityPostInfo(var postKey: String, var textPost: String, var imageList : ArrayList<String>, var postTime:String){
    constructor():this("", "", ArrayList(), "")
}
