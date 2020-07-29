package com.leds.lightcontroller.livedata

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

abstract class ParamsLiveData {
    val mediator: MediatorLiveData<String> = MediatorLiveData()
    open fun zipProperties() {

    }
}
