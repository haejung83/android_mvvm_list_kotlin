package com.haejung.template.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.haejung.template.data.Drone
import com.haejung.template.data.source.DroneRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DetailsViewModel(
    private val dronesRepository: DroneRepository
) : ViewModel() {

    private val disposable by lazy {
        CompositeDisposable()
    }

    private val _drone = MutableLiveData<Drone>()
    val drone: LiveData<Drone>
        get() = _drone

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean>
        get() = _dataLoading

    fun subscribe(droneName: String) {
        _dataLoading.value = true

        disposable.add(
            dronesRepository
                .getDrone(droneName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (!it.isPresent)
                        return@subscribe

                    _dataLoading.value = false
                    _drone.value = it.get()
                }, {
                    _dataLoading.value = false
                })
        )


    }

    fun unsubscribe() {
        disposable.clear()
    }

}