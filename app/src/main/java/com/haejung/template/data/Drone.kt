package com.haejung.template.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "drones")
data class Drone @JvmOverloads constructor(
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "type") var type: String = "",
    @ColumnInfo(name = "prop_size") var size: Int = 0,
    @ColumnInfo(name = "fc") var fc: String = "",
    @ColumnInfo(name = "image") var image: String = "",
    @PrimaryKey @ColumnInfo(name = "id") var id: String = UUID.randomUUID().toString()
)