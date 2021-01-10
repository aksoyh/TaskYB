package com.aksoyhasan.taskyb.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.aksoyhasan.taskyb.R

class NotificationFragment: Fragment(R.layout.fragment_notification) {

    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(requireActivity(), R.id.ac_ma_nav_host_fragment)
    }
}