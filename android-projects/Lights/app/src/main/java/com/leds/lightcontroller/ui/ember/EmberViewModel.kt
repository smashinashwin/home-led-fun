package com.leds.lightcontroller.ui.ember

import android.util.Log
import android.widget.SeekBar
import androidx.lifecycle.ViewModel
import com.leds.lightcontroller.data.EmberParams
import com.leds.lightcontroller.data.MqttParams
import com.leds.lightcontroller.databinding.FragmentEmberBinding
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.MqttMessage

class EmberViewModel : ViewModel() {
    init {
        Log.i("mur", "moo")
    }
    val emberParams: EmberParams = EmberParams()
    val mqttParams: MqttParams = MqttParams(
        password="Foyatgam7!".toCharArray(),
        username="bashwin",
        clientId="lightApp",
        stateTopic="state",
        patternTopic="pattern",
        lightTopic="lamp",
        serverURL="tcp://192.168.0.197")

    fun setEmberParams(seekBar: SeekBar?, binding: FragmentEmberBinding, mqttClient: MqttAndroidClient) {
        var emberParam: String = ""

        when (seekBar) {
            binding.emberDelayMinSlider -> {
                emberParams.emberDelayMin = seekBar?.progress
                emberParam = "emberDelayMin"
            }
            binding.emberDelayMaxSlider -> {
                emberParams.emberDelayMax = seekBar?.progress
                emberParam = "emberDelayMax"
            }
            binding.emberBrightenMinSlider -> {
                emberParams.emberBrightenMin = seekBar?.progress
                emberParam = "emberBrightenMin"
            }
            binding.emberBrightenMaxSlider -> {
                emberParams.emberBrightenMax = seekBar?.progress
                emberParam = "emberBrightenMax"
            }
            binding.emberDimMinSlider -> {
                emberParams.emberDimMin = seekBar?.progress
                emberParam = "emberDimMin"
            }
            binding.emberDimMaxSlider -> {
                emberParams.emberDimMax = seekBar?.progress
                emberParam = "emberDimMax"
            }
            binding.emberBrightnessTriggerMinSlider -> {
                emberParams.emberBrightnessTriggerMin = seekBar?.progress
                emberParam = "emberBrightnessTriggerMin"
            }
            binding.emberBrightnessTriggerMaxSlider -> {
                emberParams.emberBrightnessTriggerMax = seekBar?.progress
                emberParam = "emberBrightnessTriggerMax"
            }
        }
        Log.i(seekBar?.id.toString(), seekBar?.progress.toString())
        binding?.invalidateAll()
        val msg = MqttMessage()
        val payloadString = "{\"${emberParam}\":\"${seekBar?.progress}\"}"
        msg.payload = payloadString.toByteArray()
        val topic = "${mqttParams.lightTopic}/${mqttParams.patternTopic}"
        if (mqttClient.isConnected()) {
            mqttClient.publish(topic, msg)
            Log.i("emberParamChange", "sent successfully")
        }


    }

}