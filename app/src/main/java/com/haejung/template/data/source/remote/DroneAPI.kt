package com.haejung.template.data.source.remote

import com.haejung.template.data.Drone
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DroneAPI {

    @GET("/drones")
    fun requestListDrones(): Call<List<Drone>>

    @GET("/drone/{name}")
    fun requestDrone(@Path("name") name: String): Call<Drone>

}