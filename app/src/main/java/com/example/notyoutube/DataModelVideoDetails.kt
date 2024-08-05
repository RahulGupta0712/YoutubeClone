package com.example.notyoutube

import android.net.Uri
import android.os.Parcelable
import android.os.Parcel
import java.net.URI

data class DataModelVideoDetails(var key : String, var title : String, var description : String, var thumbnailUrl : String, var videoUrl: String, var videoLength : Long, var timePosted:Long) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readLong(),
        parcel.readLong()
    ) {
    }

    constructor() : this("", "", "", "", "", 0, 0)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(key)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(thumbnailUrl)
        parcel.writeString(videoUrl)
        parcel.writeLong(videoLength)
        parcel.writeLong(timePosted)
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