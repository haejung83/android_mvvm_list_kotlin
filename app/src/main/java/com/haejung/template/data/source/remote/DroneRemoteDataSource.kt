package com.haejung.template.data.source.remote

import com.haejung.template.data.Drone
import com.haejung.template.data.source.DronesDataSource
import com.orhanobut.logger.Logger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DroneRemoteDataSource : DronesDataSource {
    private val droneAPI: DroneAPI = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("http://10.0.2.2:8080")
        .build()
        .create(DroneAPI::class.java)

    override fun getDrones(callback: DronesDataSource.LoadDronesCallback) {
        droneAPI.requestListDrones().enqueue(object : Callback<List<Drone>> {
            override fun onFailure(call: Call<List<Drone>>, t: Throwable) {
                Logger.d("getDrones", "onFailure ${t.message}")
                callback.onDataNotAvailable()
            }

            override fun onResponse(call: Call<List<Drone>>, response: Response<List<Drone>>) {
                Logger.d("getDrones", "onResponse ${response.body()}")
                response.body()?.let { list ->
                    val listAddedHostAddress = list.map {
                        it.copy(image = "http://10.0.2.2:8080/drones/image/${it.name}")
                    }
                    callback.onDronesLoaded(listAddedHostAddress)
                }
            }
        })
    }

    override fun getDrone(name: String, callback: DronesDataSource.GetDroneCallback) {
        droneAPI.requestDrone(name).enqueue(object : Callback<Drone> {
            override fun onFailure(call: Call<Drone>, t: Throwable) {
                Logger.d("getDrone", "onFailure ${t.message}")
                callback.onDataNotAvailable()
            }

            override fun onResponse(call: Call<Drone>, response: Response<Drone>) {
                Logger.d("getDrone", "onResponse ${response.body()}")
                response.body()?.let { callback.onDroneLoaded(it) }
            }
        })

    }

    override fun saveDrone(drone: Drone) {
        TODO("Not implemented yet")
    }

    override fun refreshDrones() {
        TODO("Not implemented yet")
    }

    override fun deleteAllDrones() {
        TODO("Not implemented yet")
    }

    override fun deleteDrone(name: String) {
        TODO("Not implemented yet")
    }

    companion object {
        private var instance: DroneRemoteDataSource? = null

        @JvmStatic
        fun getInstance(): DroneRemoteDataSource {
            return instance ?: synchronized(this) {
                instance ?: DroneRemoteDataSource().apply {
                    instance = this
                }
            }
        }
    }

}