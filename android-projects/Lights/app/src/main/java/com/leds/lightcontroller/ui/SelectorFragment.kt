package com.leds.lightcontroller.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.leds.lightcontroller.main.MainActivity
import com.leds.lightcontroller.R
import com.leds.lightcontroller.databinding.FragmentSelectorBinding
import com.leds.lightcontroller.livedata.LightParams
import com.leds.lightcontroller.main.MainViewModel

class SelectorFragment : Fragment(), AdapterView.OnItemSelectedListener {

    companion object {
        fun newInstance() = SelectorFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentSelectorBinding
    private lateinit var paletteArray: Array<String>
    private lateinit var mainActivity: MainActivity
    lateinit var lightParams: LightParams

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mainActivity = activity as MainActivity
        val model = ViewModelProvider(mainActivity)
        viewModel = model.get(MainViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_selector, container, false)
        binding.mainViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        //TODO: make this MVC by utilizing selectorviewmodel for all the logics
        paletteArray = resources.getStringArray(R.array.palette_array)
        lightParams = viewModel.paramParams.lightParams
        val startIndex = when (lightParams.propertyMap["palette"]!!.value!!) {
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
        binding.mainViewModel = mainActivity.viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    //TODO wire up selector with live data more smartly
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        lightParams.propertyMap["palette"]!!.value = paletteArray[p2]
    }

    //should there also be a pattern selector here?

}