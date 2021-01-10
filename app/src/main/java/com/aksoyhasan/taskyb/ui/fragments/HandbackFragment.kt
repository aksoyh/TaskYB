package com.aksoyhasan.taskyb.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.aksoyhasan.taskyb.R

class HandbackFragment: Fragment(R.layout.fragment_handback) {

    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(requireActivity(), R.id.ac_ma_nav_host_fragment)
    }
}