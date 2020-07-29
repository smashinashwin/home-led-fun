package com.leds.lightcontroller.livedata

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

abstract class StringParamsLiveData: ParamsLiveData() {

    abstract val propertyNames: Array<String>
    abstract val propertyMap: MutableMap<String, MutableLiveData<String>>
    abstract val liveDataArray: Array<MutableLiveData<String>>

    override fun zipProperties() {
        liveDataArray.forEachIndexed { i, liveData: MutableLiveData<String> ->
            val propertyName = propertyNames[i]
            propertyMap[propertyName] = liveData
            mediator.addSource(liveData) { mediator.setValue(propertyName) }
        }
    }
}
