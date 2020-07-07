package com.leds.lightcontroller.ui.ember

import android.util.Log
import android.widget.SeekBar
import androidx.lifecycle.ViewModel
import com.leds.lightcontroller.MqttAndroidClientWrapper
import com.leds.lightcontroller.data.EmberParams
import com.leds.lightcontroller.data.LightParams
import com.leds.lightcontroller.databinding.FragmentEmberBinding

class EmberViewModel : ViewModel() {
    init {
        Log.i("mur", "moo")
    }
    val emberParams: EmberParams = EmberParams()

    fun setEmberParams(seekBar: SeekBar?, binding: FragmentEmberBinding, mqttClient: MqttAndroidClientWrapper, lightParams: LightParams) {
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
        binding?.invalidateAll()
        mqttClient.send(lightParams.lightTopic, 0, emberParam, seekBar?.progress.toString())

    }

}