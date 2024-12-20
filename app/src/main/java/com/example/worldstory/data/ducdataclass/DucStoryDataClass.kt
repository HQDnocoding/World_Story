package com.example.worldstory.data.ducdataclass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DucStoryDataClass(var idStory:Int, var title: String, var author : String,
                             var description: String,
                             var imgURL : String, var backgroundImageURL: String,
                             var dateCreate:String,
                             var score : Float, var isComic: Boolean): Parcelable
