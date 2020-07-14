package com.leds.lightcontroller.ui.glitter

import android.util.Log
import android.widget.SeekBar
import androidx.lifecycle.ViewModel
import com.leds.lightcontroller.data.GlitterParams
import com.leds.lightcontroller.data.MqttParams
import com.leds.lightcontroller.databinding.FragmentGlitterBinding
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.MqttMessage

class GlitterViewModel : ViewModel() {
    init {
        Log.i("mur", "moo")
    }
    var glitterParams: GlitterParams = GlitterParams()
    val mqttParams: MqttParams = MqttParams(
        password="Foyatgam7!".toCharArray(),
        username="bashwin",
        clientId="lightApp",
        stateTopic="state",
        patternTopic="pattern",
        lightTopic="lamp",
        serverURL="tcp://192.168.0.197")
    fun setGlitterParams(seekBar: SeekBar?, binding: FragmentGlitterBinding, mqttClient: MqttAndroidClient) {
        var glitterParam: String = ""

        when (seekBar) {
            binding.chanceOfGlitterSlider -> {
                glitterParams.chanceOfGlitter = seekBar?.progress
                glitterParam = "chanceOfGlitter"
            }
            binding.glitterDimDelayMinSlider -> {
                glitterParams.glitterDimDelayMin = seekBar?.progress
                glitterParam = "glitterDimDelayMin"
            }
            binding.glitterDimDelayMaxSlider -> {
                glitterParams.glitterDimDelayMax = seekBar?.progress
                glitterParam = "glitterDimDelayMax"
            }
            binding.glitterDimMinSlider -> {
                glitterParams.glitterDimMin = seekBar?.progress
                glitterParam = "glitterDimMin"
            }
            binding.glitterDimMaxSlider -> {
                glitterParams.glitterDimMax = seekBar?.progress
                glitterParam = "glitterDimMax"
            }
            binding.glitterBrightenDelayMaxSlider -> {
                glitterParams.glitterBrightenDelayMax = seekBar?.progress
                glitterParam = "glitterBrightenDelayMax"
            }
            binding.glitterBrightenDelayMinSlider -> {
                glitterParams.glitterBrightenDelayMin = seekBar?.progress
                glitterParam = "glitterBrighteDelaynMin"
            }
        }
        Log.i(seekBar?.id.toString(), seekBar?.progress.toString())
        binding?.invalidateAll()
        val msg = MqttMessage()
        val payloadString = "{\"${glitterParam}\":\"${seekBar?.progress}\"}"
        msg.payload = payloadString.toByteArray()
        val topic = "${mqttParams.lightTopic}/${mqttParams.patternTopic}"
        if (mqttClient.isConnected()) {
            mqttClient.publish(topic, msg)
            Log.i("glitterParamChange", "sent successfully")
        }


    }

}