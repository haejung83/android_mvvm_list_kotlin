package com.haejung.template.data.source

import com.haejung.template.data.Drone

interface DronesDataSource {

    interface LoadDronesCallback {
        fun onDronesLoaded(drones: List<Drone>)
        fun onDataNotAvailable()
    }

    interface GetDroneCallback {
        fun onDroneLoaded(drone: Drone)
        fun onDataNotAvailable()
    }

    fun getDrones(callback: LoadDronesCallback)

    fun getDrone(name: String, callback: GetDroneCallback)

    fun saveDrone(drone: Drone)

    fun refreshDrones()

    fun deleteAllDrones()

    fun deleteDrone(name: String)
}