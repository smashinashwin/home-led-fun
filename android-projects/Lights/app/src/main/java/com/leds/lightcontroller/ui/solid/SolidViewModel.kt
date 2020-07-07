package com.leds.lightcontroller.ui.solid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SolidViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is the solid color fragment"
    }
    val text: LiveData<String> = _text
}