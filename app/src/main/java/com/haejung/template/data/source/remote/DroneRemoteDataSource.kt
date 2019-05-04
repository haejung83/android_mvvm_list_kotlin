package com.haejung.template.data.source.remote

import com.haejung.template.data.Drone
import com.haejung.template.data.source.DronesDataSource
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class DroneRemoteDataSource : DronesDataSource {
    private val droneAPI: DroneAPI = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl("http://10.0.2.2:8080")
        .build()
        .create(DroneAPI::class.java)

    override fun getDrones(): Flowable<List<Drone>> =
        droneAPI
            .requestListDronesRx()
            .flatMap { drones -> Flowable.fromIterable(drones) }
            .map {
                it.copy(image = "http://10.0.2.2:8080/drones/image/${it.name}")
            }
            .toList()
            .toFlowable()

    override fun getDrone(name: String): Flowable<Optional<Drone>> =
        droneAPI
            .requestDroneRx(name)
            .map {
                Optional.of(
                    it.copy(image = "http://10.0.2.2:8080/drones/image/${it.name}")
                )
            }

    override fun saveDrone(drone: Drone): Completable = Completable.complete()

    override fun refreshDrones(): Completable = Completable.complete()

    override fun deleteAllDrones(): Single<Int> = Single.just(0)

    override fun deleteDrone(name: String): Single<Int> = Single.just(0)

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