package com.leds.lightcontroller.livedata

import androidx.lifecycle.MutableLiveData

/*
 * See ParamsLiveData for more information.
 */
abstract class IntParamsLiveData: ParamsLiveData() {

    abstract val propertyNames: Array<String>
    abstract val propertyMap: MutableMap<String, MutableLiveData<Int>>
    abstract val liveDataArray: Array<MutableLiveData<Int>>

    override fun zipProperties() {
        liveDataArray.forEachIndexed { i, liveData: MutableLiveData<Int> ->
            val propertyName = propertyNames[i]
            propertyMap[propertyName] = liveData
            mediator.addSource(liveData) { mediator.setValue(propertyName) }
        }
    }
}