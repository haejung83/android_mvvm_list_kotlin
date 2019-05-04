package com.haejung.template.drones

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.haejung.template.LiveEvent
import com.haejung.template.R
import com.haejung.template.data.Drone
import com.haejung.template.data.source.DroneRepository
import com.orhanobut.logger.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DronesViewModel(
    private val dronesRepository: DroneRepository
) : ViewModel() {

    private var isFirstLoad = true
    private val disposable by lazy {
        CompositeDisposable()
    }

    private val _items = MutableLiveData<List<Drone>>().apply { value = emptyList() }
    val items: LiveData<List<Drone>>
        get() = _items

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean>
        get() = _dataLoading

    private val _snackBarText = MutableLiveData<LiveEvent<Int>>()
    val snackBarMessage: LiveData<LiveEvent<Int>>
        get() = _snackBarText

    private val _openDroneEvent = MutableLiveData<LiveEvent<String>>()
    val openDroneEvent: LiveData<LiveEvent<String>>
        get() = _openDroneEvent

    val empty: LiveData<Boolean> = Transformations.map(_items) {
        it.isEmpty()
    }

    fun subscribe() {
        Logger.d("subscribe")
        if (isFirstLoad) {
            disposable.add(dronesRepository
                .refreshDrones()
                .subscribe {
                    isFirstLoad = false
                    loadDrones()
                }
            )
            return
        } else
            loadDrones()
    }

    private fun loadDrones() {
        _dataLoading.value = true
        disposable.add(
            dronesRepository
                .getDrones()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ drones ->
                    Logger.d("onNext: drones")
                    _dataLoading.value = false
                    if (drones.isNotEmpty())
                        _items.value = drones
                    else
                        showSnackBarMessage(R.string.msg_no_drones)
                }, {
                    Logger.d("onError: ${it.message}")
                    _dataLoading.value = false
                    showSnackBarMessage(R.string.msg_error)
                })
        )
    }

    fun unsubscribe() {
        Logger.d("unsubscribe")
        disposable.clear()
    }

    internal fun openDrone(name: String) {
        _openDroneEvent.value = LiveEvent(name)
    }

    private fun showSnackBarMessage(message: Int) {
        _snackBarText.value = LiveEvent(message)
    }

}