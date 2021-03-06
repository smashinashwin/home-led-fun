package com.leds.lightcontroller.livedata

import android.graphics.Color.rgb
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations

class SolidParams: IntParamsLiveData() {

    override val propertyNames: Array<String> = arrayOf(
        "solidColorRed",
        "solidColorGreen",
        "solidColorBlue",
        "solidColorWhite",
        "brightness",
        "color",
        "colorStr"
    )
    private val solidColorRed: MutableLiveData<Int> = MutableLiveData<Int>(255)
    private val solidColorGreen: MutableLiveData<Int> = MutableLiveData<Int>(50)
    private val solidColorBlue: MutableLiveData<Int> = MutableLiveData<Int>(155)
    private val solidColorWhite: MutableLiveData<Int> = MutableLiveData<Int>(255)
    val color: LiveData<Int> = Transformations.map(mediator) {returnColor()}

    override val liveDataArray: Array<MutableLiveData<Int>> = arrayOf(
        solidColorRed,
        solidColorGreen,
        solidColorBlue,
        solidColorWhite
    )

    //useful for brightness feature
    val gammaCorrection: Array<Int> = arrayOf(
        0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
        0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
        1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  2,
        2,  2,  2,  2,  2,  2,  2,  3,  3,  3,  3,  3,  3,  3,  4,  4,
        4,  4,  4,  5,  5,  5,  5,  6,  6,  6,  6,  6,  7,  7,  7,  8,
        8,  8,  8,  9,  9,  9, 10, 10, 10, 11, 11, 12, 12, 12, 13, 13,
        14, 14, 14, 15, 15, 16, 16, 17, 17, 18, 18, 19, 19, 20, 20, 21,
        22, 22, 23, 23, 24, 25, 25, 26, 27, 27, 28, 29, 29, 30, 31, 32,
        32, 33, 34, 35, 35, 36, 37, 38, 39, 40, 40, 41, 42, 43, 44, 45,
        46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 60, 61, 62,
        63, 64, 65, 67, 68, 69, 70, 72, 73, 74, 76, 77, 78, 80, 81, 82,
        84, 85, 87, 88, 90, 91, 93, 94, 96, 97, 99,101,102,104,105,107,
        109,111,112,114,116,118,119,121,123,125,127,129,131,132,134,136,
        138,140,142,144,147,149,151,153,155,157,159,162,164,166,168,171,
        173,175,178,180,182,185,187,190,192,195,197,200,202,205,207,210,
        213,215,218,221,223,226,229,232,235,237,240,243,246,249,252,255 )


    fun returnColor(): Int {
        return rgb(solidColorRed.value as Int, solidColorGreen.value as Int, solidColorBlue.value as Int)
    }

    override val propertyMap: MutableMap<String, MutableLiveData<Int>> = HashMap()
    init {
        zipProperties()
    }
}