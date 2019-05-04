package com.haejung.template.data.source

import com.haejung.template.data.Drone
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import java.util.*
import kotlin.collections.LinkedHashMap
import kotlin.math.max

class DroneRepository(
    private val droneRemoteDataSource: DronesDataSource,
    private val droneLocalDataSource: DronesDataSource
) : DronesDataSource {

    private val cachedDrones: LinkedHashMap<String, Drone> = LinkedHashMap()
    private var cacheIsDirty = false

    override fun getDrones(): Flowable<List<Drone>> {
        if (cachedDrones.isNotEmpty() && !cacheIsDirty)
            return Flowable
                .fromIterable(cachedDrones.values)
                .toList()
                .toFlowable()

        val remoteDrones = getRemoteAndSaveCacheLocal()

        return if (cacheIsDirty) {
            remoteDrones
        } else {
            val localDrone = getLocalAndSaveCache()
            Flowable.concat(localDrone, remoteDrones)
                .filter {
                    it.isNotEmpty()
                }
                .firstOrError()
                .toFlowable()
        }
    }

    private fun getLocalAndSaveCache(): Flowable<List<Drone>> =
        droneLocalDataSource
            .getDrones()
            .flatMap { drones ->
                Flowable.fromIterable(drones)
                    .doOnNext {
                        cachedDrones[it.name] = it
                    }
                    .toList()
                    .toFlowable()
            }

    private fun getRemoteAndSaveCacheLocal(): Flowable<List<Drone>> =
        droneRemoteDataSource
            .getDrones()
            .flatMap { drones ->
                Flowable.fromIterable(drones)
                    .doOnNext { drone ->
                        droneLocalDataSource
                            .saveDrone(drone)
                            .doOnComplete {
                                cachedDrones[drone.name] = drone
                            }
                            .subscribe()
                    }
                    .toList()
                    .toFlowable()
            }
            .doOnComplete {
                cacheIsDirty = false
            }

    override fun getDrone(name: String): Flowable<Optional<Drone>> {
        // Return drone if name exists in cache
        if (cachedDrones.containsKey(name)) {
            val drone = cachedDrones[name]?.let { Optional.of(it) }
            return Flowable.just(drone)
        }

        // Get a drone from local database and insert it into cache with name
        val localDrone = droneLocalDataSource
            .getDrone(name)
            .doOnNext {
                if (it.isPresent) {
                    val drone = it.get()
                    cachedDrones[drone.name] = drone
                }
            }

        // Get a drone from remote and then insert it into local database and cache
        val remoteDrone = droneRemoteDataSource
            .getDrone(name)
            .doOnNext {
                if (it.isPresent) {
                    val drone = it.get()
                    // FIXME: This is a ridiculous behavior which subscribe the saveDrone method
                    droneLocalDataSource
                        .saveDrone(drone)
                        .doOnComplete {
                            cachedDrones[drone.name] = drone
                        }
                        .subscribe()
                }
            }

        return Flowable
            .concat(localDrone, remoteDrone)
            .firstElement()
            .toFlowable()
    }

    override fun saveDrone(drone: Drone): Completable {
        val saveRemote = droneRemoteDataSource.saveDrone(drone)
        val saveLocal = droneLocalDataSource.saveDrone(drone)
        return Completable.concat(listOf(saveRemote, saveLocal))
            .doOnComplete {
                cachedDrones[drone.name] = drone
            }
    }

    override fun refreshDrones(): Completable =
        Completable
            .complete()
            .doOnComplete {
                cacheIsDirty = true
            }

    override fun deleteAllDrones(): Single<Int> {
        val deleteAllRemote = droneRemoteDataSource.deleteAllDrones()
        val deleteAllLocal = droneLocalDataSource.deleteAllDrones()
        return Single
            .zip(deleteAllRemote, deleteAllLocal, DeleteBiFunction)
    }

    override fun deleteDrone(name: String): Single<Int> {
        val deleteRemote = droneRemoteDataSource.deleteDrone(name)
        val deleteLocal = droneLocalDataSource.deleteDrone(name)
        return Single
            .zip(deleteRemote, deleteLocal, DeleteBiFunction)
    }

    private object DeleteBiFunction : BiFunction<Int, Int, Int> {
        override fun apply(t1: Int, t2: Int): Int {
            return if (t1 == t2) t1 else max(t1, t2)
        }
    }

    companion object {
        private var instance: DroneRepository? = null

        @JvmStatic
        fun getInstance(
            droneRemoteDataSource: DronesDataSource,
            droneLocalDataSource: DronesDataSource
        ): DroneRepository {
            return instance ?: synchronized(this) {
                instance ?: DroneRepository(droneRemoteDataSource, droneLocalDataSource).apply {
                    instance = this
                }
            }
        }
    }

}