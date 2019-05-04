package com.haejung.template.data.source

import com.haejung.template.data.Drone
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*

interface DronesDataSource {
    fun getDrones(): Flowable<List<Drone>>

    fun getDrone(name: String): Flowable<Optional<Drone>>

    fun saveDrone(drone: Drone): Completable

    fun refreshDrones(): Completable

    fun deleteAllDrones(): Single<Int>

    fun deleteDrone(name: String): Single<Int>
}