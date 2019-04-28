package com.haejung.template.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.haejung.template.data.Drone
import com.haejung.template.data.source.DroneRepository
import com.haejung.template.data.source.DronesDataSource

class DetailsViewModel(
    private val dronesRepository: DroneRepository
) : ViewModel() {

    private val _drone = MutableLiveData<Drone>()
    val drone: LiveData<Drone>
        get() = _drone

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean>
        get() = _dataLoading

    fun start(droneName: String) {
        _dataLoading.value = true
        dronesRepository.getDrone(droneName, object : DronesDataSource.GetDroneCallback {
            override fun onDroneLoaded(drone: Drone) {
                _dataLoading.value = false
                _drone.value = drone
            }

            override fun onDataNotAvailable() {
                _dataLoading.value = false
            }
        })
    }

}