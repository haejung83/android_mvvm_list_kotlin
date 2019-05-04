package com.haejung.template.data.source.local

import androidx.room.*
import com.haejung.template.data.Drone
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*

@Dao
interface DroneDao {

    @Query("select * from Drones")
    fun findAll(): Flowable<List<Drone>>

    @Query("select * from Drones where id = :id")
    fun findById(id: String): Flowable<Optional<Drone>>

    @Query("select * from Drones where name = :name")
    fun findByName(name: String): Flowable<List<Drone>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(drone: Drone): Completable

    @Update
    fun update(drone: Drone): Completable

    @Query("update drones set type = :type where id = :id ")
    fun updateById(id: String, type: String): Completable

    @Query("update drones set type = :type where name = :name ")
    fun updateByName(name: String, type: String): Completable

    @Query("delete from drones where id = :id")
    fun deleteById(id: String): Single<Int>

    @Query("delete from drones where name = :name")
    fun deleteByName(name: String): Single<Int>

    @Query("delete from drones")
    fun deleteAll(): Single<Int>

}