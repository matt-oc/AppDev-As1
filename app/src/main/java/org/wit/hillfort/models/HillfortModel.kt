package org.wit.hillfort.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Embedded
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class HillfortModel(@PrimaryKey(autoGenerate = true)var id: Long = 0,
                          var title: String = "",
                          var description: String = "",
                          var visited: Boolean = false,
                          var notes: String = "",
                          var image: String = "",
                          var date: String = "",
                          var fbId : String = "",
                         @Embedded var location : Location = Location()): Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable

