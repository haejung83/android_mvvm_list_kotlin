package com.haejung.template.data.source.local

import androidx.room.*
import com.haejung.template.data.Drone

@Dao
interface DroneDao {

    @Query("select * from Drones")
    fun findAll(): List<Drone>

    @Query("select * from Drones where id = :id")
    fun findById(id: String): Drone

    @Query("select * from Drones where name = :name")
    fun findByName(name: String): List<Drone>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(drone: Drone)

    @Update
    fun update(drone: Drone)

    @Query("update drones set type = :type where id = :id ")
    fun updateById(id: String, type: String)

    @Query("update drones set type = :type where name = :name ")
    fun updateByName(name: String, type: String)

    @Query("delete from drones where id = :id")
    fun deleteById(id: String): Int

    @Query("delete from drones where name = :name")
    fun deleteByName(name: String): Int

    @Query("delete from drones")
    fun deleteAll()

}