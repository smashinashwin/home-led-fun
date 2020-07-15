package com.leds.lightcontroller.ui.solid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.leds.lightcontroller.data.EmberParams
import com.leds.lightcontroller.data.SolidParams

class SolidViewModel : ViewModel() {
    val solidParams: SolidParams = SolidParams()

    private fun setBrightness(brightness: Int) {
        
    }

}