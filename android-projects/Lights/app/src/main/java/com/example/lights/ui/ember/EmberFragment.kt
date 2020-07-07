package com.example.lights.ui.ember

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.fragment.app.Fragment
import com.example.lights.R
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.lights.MainActivity
import com.example.lights.databinding.FragmentEmberBinding


class EmberFragment : Fragment(), SeekBar.OnSeekBarChangeListener {

    private lateinit var viewModel: EmberViewModel
    private lateinit var binding: FragmentEmberBinding
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
        viewModel = model.get(EmberViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ember, container, false)
        binding.emberParams = viewModel.emberParams

        //this should be done in the selector fragment view model.
        //need to figure out how that wires up
        mainActivity.updatePattern(0)
        this.addListenersToSliders(binding)

        return binding.root
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        viewModel.setEmberParams(seekBar, binding, mainActivity.mqttClient)
    }

    override fun onStartTrackingTouch(p0: SeekBar?) = Unit

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        viewModel.setEmberParams(seekBar, binding, mainActivity.mqttClient)
    }

    private fun addListenersToSliders(binding: FragmentEmberBinding) {
        val sliders = listOf<AppCompatSeekBar>(
            binding.emberDelayMinSlider, binding.emberDelayMaxSlider,
            binding.emberBrightenMinSlider, binding.emberBrightenMaxSlider,
            binding.emberDimMinSlider, binding.emberDimMaxSlider,
            binding.emberBrightnessTriggerMinSlider, binding.emberBrightnessTriggerMaxSlider
        )

        for (slider in sliders) slider!!.setOnSeekBarChangeListener(this)
    }
}


