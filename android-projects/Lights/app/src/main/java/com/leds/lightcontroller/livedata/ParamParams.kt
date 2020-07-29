package com.leds.lightcontroller.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

//this is really similar to a params live data
//but a couple of differences that I'm not sure how best to deal with
//option one is just remove the inheritance; there's no code saving on this one anyway
//option two is to make the ParamsLiveData class an interface / more abstract
//I'd still have the benefit of all of these inheriting the same class, but lose the code reuse benefit.
//will mess around with that in a future branch. this is a fine hacky thing for now.


//this class contains a super-mediator: it listens to all of the mediators in its member livedata classes
//Those mediators in turn listen to all of their member live data
//With this super-mediator, we should only have to observe on mediator, and call one function in the master live data model that handles all light changes
//Should reduce the amount of code required.
//the mediator will contain the name of the member variable most recently changed.
class ParamParams {
    val propertyNames: Array<String> = arrayOf(
        "emberParams",
        "glitterParams",
        "lightParams",
        "solidParams"
    )

    val emberParams: EmberParams = EmberParams()
    val glitterParams: GlitterParams = GlitterParams()
    val lightParams: LightParams = LightParams()
    val solidParams: SolidParams = SolidParams()

    val mediatorArray: Array<MediatorLiveData<String>> = arrayOf(
        emberParams.mediator,
        glitterParams.mediator,
        lightParams.mediator,
        solidParams.mediator
    )
    val liveDataArray: Array<ParamsLiveData> = arrayOf(
        emberParams,
        glitterParams,
        lightParams,
        solidParams
    )
    val propertyMap: MutableMap<String, ParamsLiveData> = HashMap()
    val propMap: MutableMap<String, ParamsLiveData> = HashMap()

    val mediator: MediatorLiveData<String> = MediatorLiveData()

    init {
        zipProperties()
    }

    private fun zipProperties() {
        mediatorArray.forEachIndexed { i, thatMediator: MediatorLiveData<String> ->
            val propertyName = propertyNames[i]
            propMap[propertyName] = liveDataArray[i]
            mediator.addSource(thatMediator) {mediator.setValue(propertyName)}
        }
    }
}