package com.leds.lightcontroller.livedata

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
/*
 * This interface is implemented by abstract classes IntParamsLiveData and StringParamsLiveData.
 * All configurable light parameters are live-data types, and are two-way bound to the layout.
 *
 * medidator: MediatorLiveData observes all other live data objects within any implementing class, and updates to the NAME of that live data object.
 * ParamParams is a class that hold all concrete impletementations of ParamsLiveData.
 * It also implements ParamsLiveData. Its mediator observes all of its member ParamsLiveData objects and is observed in the main activity, which kicks off an MQTT send process.
 * With this pattern, the UI only has one observer, and one call for sending MQTT messages.
 */
abstract class ParamsLiveData {
    val mediator: MediatorLiveData<String> = MediatorLiveData()
    open fun zipProperties() {

    }
}
