package com.example.notyoutube

// key : key of video in realtime database
// videoId : id of video in firestore database
// channelId : key of user in realtime database

data class DataModelVideoDetails(var key : String, var title : String, var description : String, var thumbnailUrl : String, var videoUrl: String, var videoLength : Long, var timePosted:Long, var visibility:String, var channelName:String, var profileUrl : String, var channelId : String, var videoId:String, var likesCount:Long, var commentCount:Long, var viewsCount:Long) {
    constructor():this("", "", "", "", "", 0, 0, "","","","","",0,0,0)
}