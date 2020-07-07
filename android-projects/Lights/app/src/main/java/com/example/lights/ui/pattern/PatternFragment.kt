package com.example.lights.ui.pattern

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.lights.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.*
import com.google.android.material.internal.NavigationMenuItemView
import com.google.android.material.navigation.NavigationView
import com.example.lights.databinding.FragmentPatternBinding
import kotlinx.android.synthetic.main.fragment_pattern.view.*

class PatternFragment : Fragment() {

    private lateinit var patternViewModel: PatternViewModel
    private lateinit var binding: FragmentPatternBinding

    //use mqtt to get and set the light and pattern params data classes
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        //how to setup binding here?
        binding = DataBindingUtil.inflate<FragmentPatternBinding>(inflater, R.layout.fragment_pattern, container, false)
        patternViewModel =
                ViewModelProviders.of(this).get(PatternViewModel::class.java)
        //val root ?? or binding?
        //val root = inflater.inflate(R.layout.fragment_pattern, container, false)
        val nav = findNavController()

        setHasOptionsMenu(true)
        val menu = binding.root.pattern_menu
        menu?.setupWithNavController(nav)
        val textView: TextView = binding.textPattern
        patternViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.pattern_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item!!, findNavController())
        || super.onOptionsItemSelected(item)
    }
}