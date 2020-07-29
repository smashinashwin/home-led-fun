package com.leds.lightcontroller.main

import android.text.PrecomputedText
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.leds.lightcontroller.livedata.*
import com.leds.lightcontroller.utils.MqttAndroidClientWrapper

class MainViewModel() : ViewModel() {
    lateinit var mqttClient: MqttAndroidClientWrapper
    //this thing has all the paramaters.
    lateinit var paramParams: ParamParams

    fun initialize(activity: MainActivity) {
        mqttClient = MqttAndroidClientWrapper(activity)
        paramParams = ParamParams()
    }

    fun connectMqtt() {
        if (!mqttClient.isConnected()) mqttClient.connectMqtt()
    }

    //TODO: bind the stateOn variable to the power switch. Set the color in XML
    //TODO: convert all hard-coded strings to constants
    //TODO: should these functions belong in the data classes?
    //TODO: can this be generic enough to always send?
    fun sendMessage(propertyName: String) {
        //choose the glitter or ember object, find the changed parameter and value, and send.
        val property: ParamsLiveData? = paramParams.propMap[propertyName]
        val parameter: String = property!!.mediator.value!!
        val lightParams = paramParams.lightParams
        val lightTopic = lightParams.propertyMap["lightTopic"]!!.value!!

        if (property is IntParamsLiveData) {
            val parameterValue: Int = property.propertyMap[parameter]!!.value!!
            mqttClient.send(lightTopic, 0, parameter, parameterValue)
        }
        else if (property is StringParamsLiveData){
            val parameterValue: String = property.propertyMap[parameter]!!.value!!
            var stateOrPattern = 0
            if (parameter == "stateOn") stateOrPattern = 1
            mqttClient.send(lightTopic, stateOrPattern, parameter, parameterValue)
        }
    }

    fun flipSwitch() {
        val lightParams = paramParams.lightParams
        val powerStatus = lightParams.propertyMap["stateOn"]!!.value == "false"
        lightParams.propertyMap["stateOn"]!!.value = if (powerStatus)  "true" else "false"
    }
}