package com.haejung.template

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.haejung.template.data.injectRepository
import com.haejung.template.data.source.DroneRepository
import com.haejung.template.details.DetailsViewModel
import com.haejung.template.drones.DronesViewModel

class ViewModelFactory private constructor(
    private val dronesRepository: DroneRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        with(modelClass) {
            when {
                isAssignableFrom(DronesViewModel::class.java) ->
                    DronesViewModel(dronesRepository)
                isAssignableFrom(DetailsViewModel::class.java) ->
                    DetailsViewModel(dronesRepository)
                else ->
                    throw IllegalArgumentException()
            }
        } as T

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(application: Application): ViewModelFactory {
            return instance ?: synchronized(ViewModelFactory::class.java) {
                instance ?: ViewModelFactory(injectRepository(application.applicationContext)).also {
                    instance = it
                }
            }
        }
    }

}