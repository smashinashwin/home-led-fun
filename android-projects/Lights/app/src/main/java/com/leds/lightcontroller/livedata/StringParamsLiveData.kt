package com.leds.lightcontroller.livedata

import androidx.lifecycle.MutableLiveData

/*
 * See ParamsLiveData for more information.
 */
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
