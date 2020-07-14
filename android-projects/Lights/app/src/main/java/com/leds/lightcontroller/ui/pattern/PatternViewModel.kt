package com.leds.lightcontroller.ui.pattern

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PatternViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "__"
    }
    val text: LiveData<String> = _text
}