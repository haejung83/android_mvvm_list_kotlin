package com.haejung.template.data.source.remote

import com.haejung.template.data.Drone
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Path

interface DroneAPI {

    @GET("/drones")
    fun requestListDronesRx(): Flowable<List<Drone>>

    @GET("/drones/{name}")
    fun requestDroneRx(@Path("name") name: String): Flowable<Drone>

}