package com.example.notyoutube

data class CommentModel(var comment : String, var userId : String, var likesCount : Long, var timePosted:Long,  var replyCount:Long){
    constructor():this("", "", 0, 0, 0)
}
