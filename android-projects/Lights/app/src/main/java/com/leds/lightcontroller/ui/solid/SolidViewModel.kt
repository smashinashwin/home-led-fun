package com.leds.lightcontroller.ui.solid

import android.graphics.Color
import android.util.Log
import android.widget.SeekBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.leds.lightcontroller.MqttAndroidClientWrapper
import com.leds.lightcontroller.data.EmberParams
import com.leds.lightcontroller.data.LightParams
import com.leds.lightcontroller.data.SolidParams
import com.leds.lightcontroller.databinding.FragmentEmberBinding
import com.leds.lightcontroller.databinding.FragmentSolidBinding

class SolidViewModel : ViewModel() {
    val solidParams: SolidParams = SolidParams()

    private fun setColor() {
        solidParams.color = Color.rgb(solidParams.solidColorRed, solidParams.solidColorGreen ,solidParams.solidColorBlue)
        solidParams.colorStr = "#" + Integer.toHexString(solidParams.color)
    }

    private fun setBrightness(brightness: Int) {
        solidParams.solidColorRed = solidParams.gammaCorrection[brightness] * solidParams.solidColorRed / 255
        solidParams.solidColorGreen = solidParams.gammaCorrection[brightness] * solidParams.solidColorGreen / 255
        solidParams.solidColorBlue = solidParams.gammaCorrection[brightness] * solidParams.solidColorBlue / 255
        solidParams.solidColorWhite = solidParams.gammaCorrection[brightness] * solidParams.solidColorWhite / 255
        solidParams.brightness = brightness
        setColor()
    }
    fun setSolidParams(seekBar: SeekBar?, binding: FragmentSolidBinding, mqttClient: MqttAndroidClientWrapper, lightParams: LightParams) {
        var emberParam: String = ""

        when (seekBar) {
            binding.redSlider -> {
                solidParams.solidColorRed = seekBar?.progress
                setColor()
            }
            binding.greenSlider -> {
                solidParams.solidColorGreen = seekBar?.progress
                setColor()
            }
            binding.blueSlider -> {
                solidParams.solidColorBlue = seekBar?.progress
                setColor()
            }
            binding.whiteSlider -> {
                solidParams.solidColorWhite = seekBar?.progress
                setColor()
            }
            binding.brightnessSlider -> {
                this.setBrightness(seekBar?.progress)
            }
        }
        binding?.invalidateAll()
        val rgbw = arrayOf<Int>(solidParams.solidColorRed, solidParams.solidColorGreen, solidParams.solidColorBlue, solidParams.solidColorWhite)
        mqttClient.send(lightParams.lightTopic, 0, "rgbw", rgbw)

    }

}