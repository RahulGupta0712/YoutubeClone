package com.example.notyoutube


import android.os.Parcelable
import android.os.Parcel

// key : key of video in realtime database
// videoId : id of video in firestore database
// channelId : key of user in realtime database

data class DataModelVideoDetails(var key : String, var title : String, var description : String, var thumbnailUrl : String, var videoUrl: String, var videoLength : Long, var timePosted:Long, var visibility:String, var channelName:String, var profileUrl : String, var channelId : String, var videoId:String = "") : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:""
    ) {
    }

    constructor() : this("", "", "", "", "", 0, 0, "", "", "","", "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(key)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(thumbnailUrl)
        parcel.writeString(videoUrl)
        parcel.writeLong(videoLength)
        parcel.writeLong(timePosted)
        parcel.writeString(visibility)
        parcel.writeString(channelName)
        parcel.writeString(profileUrl)
        parcel.writeString(channelId)
        parcel.writeString(videoId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DataModelVideoDetails> {
        override fun createFromParcel(parcel: Parcel): DataModelVideoDetails {
            return DataModelVideoDetails(parcel)
        }

        override fun newArray(size: Int): Array<DataModelVideoDetails?> {
            return arrayOfNulls(size)
        }
    }
}