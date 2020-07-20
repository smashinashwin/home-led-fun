package com.leds.lightcontroller.ui.selector

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import com.leds.lightcontroller.MainActivity
import com.leds.lightcontroller.R
import com.leds.lightcontroller.data.LightParams
import com.leds.lightcontroller.data.MqttParams
import com.leds.lightcontroller.databinding.FragmentSelectorBinding
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.MqttMessage

class SelectorFragment : Fragment(), AdapterView.OnItemSelectedListener {

    companion object {
        fun newInstance() = SelectorFragment()
    }

    private lateinit var viewModel: SelectorViewModel
    private lateinit var binding: FragmentSelectorBinding
    lateinit var lightParams: LightParams
    lateinit var paletteArray: Array<String>
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //TODO: make this MVC by utilizing selectorviewmodel for all the logics
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_selector, container, false)
        lightParams = LightParams()
        paletteArray = resources.getStringArray(R.array.palette_array)

        val startIndex = when (lightParams.palette) {
            paletteArray.get(0) -> 0
            paletteArray.get(1) -> 1
            paletteArray.get(2) -> 2
            paletteArray.get(3) -> 3
            paletteArray.get(4) -> 4
            paletteArray.get(5) -> 5
            paletteArray.get(6) -> 6
            paletteArray.get(7) -> 7
            else -> 7
        }

        val spinner: Spinner = binding.paletteSpinner
        ArrayAdapter.createFromResource(
            this.requireContext(),
            R.array.palette_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        spinner.setSelection(startIndex)

        spinner.onItemSelectedListener = this
        this.mainActivity = activity as MainActivity


        return binding.root


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SelectorViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        lightParams.palette = paletteArray.get(p2)
        binding.invalidateAll()
        Log.i("selectorfragment", lightParams.palette)
        mainActivity.mqttClient.send(lightParams.lightTopic, 0, "palette", lightParams.palette)
    }

    //should there also be a pattern selector here?

}