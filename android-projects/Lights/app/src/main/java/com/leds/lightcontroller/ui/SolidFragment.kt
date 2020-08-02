package com.leds.lightcontroller.ui

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.leds.lightcontroller.main.MainActivity
import com.leds.lightcontroller.R
import com.leds.lightcontroller.databinding.FragmentSolidBinding
import com.leds.lightcontroller.main.MainViewModel

/*
 * All this fragment need to do is draw the layout to the screen. and setup two-way data binding.
 */
class SolidFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentSolidBinding
    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        val model = ViewModelProvider(mainActivity)
        viewModel = model.get(MainViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_solid, container, false)

        binding.mainViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }
    /*
     * The palette selector isn't relevant for this view, so hide it.
     */
    override fun onStart() {
        super.onStart()
        val lightParams = mainActivity.viewModel.paramParams.lightParams
        lightParams.propertyMap["pattern"]!!.value = getString(R.string.solidpatternnumber)
        binding.navigationSelector.findViewById<View>(R.id.palette_spinner).visibility = View.GONE
        binding.navigationSelector.findViewById<View>(R.id.palette_selector).visibility = View.GONE

    }
}