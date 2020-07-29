package com.leds.lightcontroller.livedata

import androidx.lifecycle.MutableLiveData

class GlitterParams: IntParamsLiveData() {

    override val propertyNames: Array<String> = arrayOf(
        "chanceOfGlitter",
        "glitterDimDelayMin",
        "glitterDimDelayMax",
        "glitterDimMin",
        "glitterDimMax",
        "glitterBrightenDelayMin",
        "glitterBrightenDelayMax"
    )
    private val chanceOfGlitter: MutableLiveData<Int> = MutableLiveData<Int>(200)
    private val glitterDimDelayMin: MutableLiveData<Int> = MutableLiveData<Int>(4)
    private val glitterDimDelayMax: MutableLiveData<Int> = MutableLiveData<Int>(15)
    private val glitterDimMin: MutableLiveData<Int> = MutableLiveData<Int>(3)
    private val glitterDimMax: MutableLiveData<Int> = MutableLiveData<Int>(50)
    private val glitterBrightenDelayMin: MutableLiveData<Int> = MutableLiveData<Int>(0)
    private val glitterBrightenDelayMax: MutableLiveData<Int> = MutableLiveData<Int>(0)

    override val liveDataArray: Array<MutableLiveData<Int>> = arrayOf(
        chanceOfGlitter,
        glitterDimDelayMin,
        glitterDimDelayMax,
        glitterDimMin,
        glitterDimMax,
        glitterBrightenDelayMin,
        glitterBrightenDelayMax
    )
    override val propertyMap: MutableMap<String, MutableLiveData<Int>> = HashMap()
    init {
        zipProperties()
    }
}

