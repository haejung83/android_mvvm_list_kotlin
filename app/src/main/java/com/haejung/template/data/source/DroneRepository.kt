package com.haejung.template.data.source

import com.haejung.template.data.Drone

class DroneRepository(
    private val droneRemoteDataSource: DronesDataSource,
    private val droneLocalDataSource: DronesDataSource
) : DronesDataSource {

    val cachedDrones: LinkedHashMap<String, Drone> = LinkedHashMap()
    var cacheIsDirty = true

    override fun getDrones(callback: DronesDataSource.LoadDronesCallback) {
        if (cachedDrones.isNotEmpty() && !cacheIsDirty) {
            callback.onDronesLoaded(cachedDrones.values.toList())
        }

        if (cacheIsDirty) {
            // Get from remote and replace local and cache
            getFromRemoteDataSource(callback)
        } else {
            droneLocalDataSource.getDrones(object : DronesDataSource.LoadDronesCallback {
                override fun onDronesLoaded(drones: List<Drone>) {
                    refreshCache(drones)
                    callback.onDronesLoaded(drones)
                }

                override fun onDataNotAvailable() {
                    getFromRemoteDataSource(callback)
                }
            })
        }
    }

    private fun getFromRemoteDataSource(callback: DronesDataSource.LoadDronesCallback) {
        droneRemoteDataSource.getDrones(object : DronesDataSource.LoadDronesCallback {
            override fun onDronesLoaded(drones: List<Drone>) {
                refreshCache(drones)
                refreshLocalDataSource(drones)
                callback.onDronesLoaded(cachedDrones.values.toList())
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        })
    }

    private fun refreshCache(drones: List<Drone>) {
        cachedDrones.clear()
        drones.forEach {
            cacheAndPerform(it) {}
        }
        cacheIsDirty = false
    }

    private fun refreshLocalDataSource(drones: List<Drone>) {
        droneLocalDataSource.deleteAllDrones()
        drones.forEach {
            droneLocalDataSource.saveDrone(it)
        }
    }

    override fun getDrone(name: String, callback: DronesDataSource.GetDroneCallback) {
        val cached = cachedDrones[name]

        if (cached != null) {
            callback.onDroneLoaded(cached)
            return
        }

        droneLocalDataSource.getDrone(name, object : DronesDataSource.GetDroneCallback {
            override fun onDroneLoaded(drone: Drone) {
                cacheAndPerform(drone) {
                    callback.onDroneLoaded(it)
                }
            }

            override fun onDataNotAvailable() {
                droneRemoteDataSource.getDrone(name, object : DronesDataSource.GetDroneCallback {
                    override fun onDroneLoaded(drone: Drone) {
                        cacheAndPerform(drone) {
                            callback.onDroneLoaded(it)
                        }
                    }

                    override fun onDataNotAvailable() {
                        callback.onDataNotAvailable()
                    }
                })
            }
        })

    }

    override fun saveDrone(drone: Drone) {
        cacheAndPerform(drone) {
            droneRemoteDataSource.saveDrone(it)
            droneLocalDataSource.saveDrone(it)
        }
    }

    override fun refreshDrones() {
        cacheIsDirty = true
    }

    override fun deleteAllDrones() {
        droneRemoteDataSource.deleteAllDrones()
        droneLocalDataSource.deleteAllDrones()
        cachedDrones.clear()
    }

    override fun deleteDrone(name: String) {
        droneRemoteDataSource.deleteDrone(name)
        droneLocalDataSource.deleteDrone(name)
        cachedDrones.remove(name)
    }

    private inline fun cacheAndPerform(drone: Drone, perform: (Drone) -> Unit) {
        val cached = drone.copy()
        cachedDrones[cached.name] = cached
        perform(cached)
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