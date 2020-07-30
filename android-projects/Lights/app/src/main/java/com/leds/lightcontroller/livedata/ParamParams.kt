package com.leds.lightcontroller.livedata

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.MediatorLiveData
import kotlinx.android.parcel.Parcelize

@Parcelize
class ParamParams: ParamsLiveData(), Parcelable {
    private val propertyNames: Array<String> = arrayOf(
        "emberParams",
        "glitterParams",
        "lightParams",
        "solidParams"
    )

    val emberParams: EmberParams = EmberParams()
    val glitterParams: GlitterParams = GlitterParams()
    val lightParams: LightParams = LightParams()
    val solidParams: SolidParams = SolidParams()

    private val mediatorArray: Array<MediatorLiveData<String>> = arrayOf(
        emberParams.mediator,
        glitterParams.mediator,
        lightParams.mediator,
        solidParams.mediator
    )
    private val liveDataArray: Array<ParamsLiveData> = arrayOf(
        emberParams,
        glitterParams,
        lightParams,
        solidParams
    )
    val propertyMap: MutableMap<String, ParamsLiveData> = HashMap()

    init {
        zipProperties()
    }

    override fun zipProperties() {
        mediatorArray.forEachIndexed { i, thatMediator: MediatorLiveData<String> ->
            val propertyName = propertyNames[i]
            propertyMap[propertyName] = liveDataArray[i]
            mediator.addSource(thatMediator) {mediator.setValue(propertyName)}
        }
    }
}