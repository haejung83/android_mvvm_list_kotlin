package com.haejung.template.data.source.local

import com.haejung.template.data.Drone
import com.haejung.template.data.source.DronesDataSource
import com.haejung.template.util.AppExecutors
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*

class DroneLocalDataSource private constructor(
    private val appExecutor: AppExecutors,
    private val droneDao: DroneDao
) : DronesDataSource {

    // Get all drone
    override fun getDrones(): Flowable<List<Drone>> = droneDao
        .findAll()

    // Get a drone with name
    override fun getDrone(name: String): Flowable<Optional<Drone>> = droneDao
        .findByName(name)
        .flatMap { drones -> Flowable.fromIterable(drones) }
        .firstElement()
        .map { Optional.of(it) }
        .toFlowable()

    override fun saveDrone(drone: Drone): Completable = droneDao.save(drone)

    override fun refreshDrones(): Completable = Completable.complete()

    override fun deleteAllDrones(): Single<Int> = droneDao.deleteAll()

    override fun deleteDrone(name: String): Single<Int> = droneDao.deleteByName(name)

    companion object {
        private var instance: DroneLocalDataSource? = null

        @JvmStatic
        fun getInstance(appExecutor: AppExecutors, droneDao: DroneDao): DroneLocalDataSource {
            return instance ?: synchronized(this) {
                instance ?: DroneLocalDataSource(appExecutor, droneDao).apply {
                    instance = this
                }
            }
        }
    }

}