package com.leds.lightcontroller.ui.glitter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.fragment.app.Fragment
import com.leds.lightcontroller.R
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.leds.lightcontroller.MainActivity
import com.leds.lightcontroller.databinding.FragmentGlitterBinding


class GlitterFragment : Fragment(), SeekBar.OnSeekBarChangeListener {

    private lateinit var viewModel: GlitterViewModel
    private lateinit var binding: FragmentGlitterBinding
    lateinit var mainActivity: MainActivity
    //lateinit var selectorFragment: SelectorFragment

    //data binding variables from the EmberParams class. https://classroom.udacity.com/courses/ud9012/lessons/4f6d781c-3803-4cb9-b08b-8b5bcc318d1c/concepts/68f0a220-8b33-43fa-be12-1110578a0e1b
    //use mqtt to get them.
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        //this is where you want to mqtt to the light to activate the ember fragment
        mainActivity = activity as MainActivity
        val model = ViewModelProvider(this)
        viewModel = model.get(GlitterViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_glitter, container, false)
        binding.glitterParams = viewModel.glitterParams

        //this should be done in the selector fragment view model.
        //need to figure out how that wires up
        mainActivity.mqttClient.send(mainActivity.lightParams.lightTopic, 0, "pattern", "1")
        this.addListenersToSliders(binding)
        return binding.root
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        viewModel.setGlitterParams(seekBar, binding, mainActivity.mqttClient, mainActivity.lightParams)
    }

    override fun onStartTrackingTouch(p0: SeekBar?) = Unit

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        viewModel.setGlitterParams(seekBar, binding, mainActivity.mqttClient, mainActivity.lightParams)
    }

    private fun addListenersToSliders(binding: FragmentGlitterBinding) {
        val glitterSliders = listOf<AppCompatSeekBar>(
            binding.chanceOfGlitterSlider, binding.glitterDimDelayMinSlider,
            binding.glitterDimDelayMaxSlider, binding.glitterDimMinSlider,
            binding.glitterDimMaxSlider, binding.glitterBrightenDelayMinSlider,
            binding.glitterBrightenDelayMaxSlider)

        for (slider in glitterSliders) slider!!.setOnSeekBarChangeListener(this)
    }
}


