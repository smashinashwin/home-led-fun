package com.leds.lightcontroller.ui.solid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.leds.lightcontroller.R

class SolidFragment : Fragment() {

    private lateinit var solidViewModel: SolidViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        solidViewModel =
                ViewModelProviders.of(this).get(SolidViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_solid, container, false)
        val textView: TextView = root.findViewById(R.id.text_solid)
        solidViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}