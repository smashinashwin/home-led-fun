package com.leds.lightcontroller.livedata

import androidx.lifecycle.MutableLiveData

class EmberParams : IntParamsLiveData() {

    override val propertyNames: Array<String> = arrayOf(
        "emberDelayMin",
        "emberDelayMax",
        "emberBrightenMin",
        "emberBrightenMax",
        "emberDimMin",
        "emberDimMax",
        "emberBrightnessTriggerMin",
        "emberBrightnessTriggerMax"
    )
    private val emberDelayMin: MutableLiveData<Int> = MutableLiveData<Int>(0)
    private val emberDelayMax: MutableLiveData<Int> = MutableLiveData<Int>(0)
    private val emberBrightenMin: MutableLiveData<Int> = MutableLiveData<Int>(1)
    private val emberBrightenMax: MutableLiveData<Int> = MutableLiveData<Int>(5)
    private val emberDimMin: MutableLiveData<Int> = MutableLiveData<Int>(1)
    private val emberDimMax: MutableLiveData<Int> = MutableLiveData<Int>(5)
    private val emberBrightnessTriggerMin: MutableLiveData<Int> = MutableLiveData<Int>(220)
    private val emberBrightnessTriggerMax: MutableLiveData<Int> = MutableLiveData<Int>(220)

    override val liveDataArray: Array<MutableLiveData<Int>> = arrayOf(
        emberDelayMin,
        emberDelayMax,
        emberBrightenMin,
        emberBrightenMax,
        emberDimMin,
        emberDimMax,
        emberBrightnessTriggerMin,
        emberBrightnessTriggerMax
    )
    override val propertyMap: MutableMap<String, MutableLiveData<Int>> = HashMap()
    init {
        zipProperties()
    }
}