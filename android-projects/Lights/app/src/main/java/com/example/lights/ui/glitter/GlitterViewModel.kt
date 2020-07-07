package com.example.lights.ui.glitter

import android.util.Log
import android.widget.SeekBar
import androidx.lifecycle.ViewModel
import com.example.lights.MainActivity
import com.example.lights.data.EmberParams
import com.example.lights.data.GlitterParams
import com.example.lights.data.MqttParams
import com.example.lights.databinding.FragmentGlitterBinding
import com.google.android.material.internal.ContextUtils.getActivity
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.MqttMessage

class GlitterViewModel : ViewModel() {
    init {
        Log.i("mur", "moo")
    }
    var glitterParams: GlitterParams = GlitterParams()
    val mqttParams: MqttParams = MqttParams()
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