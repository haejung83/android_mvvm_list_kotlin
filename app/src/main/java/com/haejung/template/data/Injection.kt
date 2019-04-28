package com.haejung.template.data

import android.content.Context
import com.haejung.template.data.source.DroneRepository
import com.haejung.template.data.source.local.DroneDatabase
import com.haejung.template.data.source.local.DroneLocalDataSource
import com.haejung.template.data.source.remote.DroneRemoteDataSource
import com.haejung.template.util.AppExecutors

fun injectRepository(context: Context): DroneRepository {
    val db = DroneDatabase.getInstance(context)
    val appExecutors = AppExecutors()
    return DroneRepository.getInstance(
        DroneRemoteDataSource.getInstance(),
        DroneLocalDataSource.getInstance(appExecutors, db.droneDao())
    )
}