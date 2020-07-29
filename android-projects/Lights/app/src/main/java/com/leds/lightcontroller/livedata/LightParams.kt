package com.leds.lightcontroller.livedata

import androidx.lifecycle.MutableLiveData

class LightParams: StringParamsLiveData() {

    override val propertyNames: Array<String> = arrayOf(
        "lightTopic",
        "stateOn",
        "palette",
        "pattern"
    )
    private val lightTopic: MutableLiveData<String> = MutableLiveData<String>("lamp")
    private val stateOn: MutableLiveData<String> = MutableLiveData<String>("false")
    private val palette: MutableLiveData<String> = MutableLiveData<String>("allStars")
    private val pattern: MutableLiveData<String> = MutableLiveData<String>("0")

    override val liveDataArray: Array<MutableLiveData<String>> = arrayOf(
        lightTopic,
        stateOn,
        palette,
        pattern
    )
    override val propertyMap: MutableMap<String, MutableLiveData<String>> = HashMap()
    init {
        zipProperties()
    }
}