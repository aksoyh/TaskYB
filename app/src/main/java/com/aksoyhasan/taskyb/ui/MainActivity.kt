package com.aksoyhasan.taskyb.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.aksoyhasan.taskyb.R
import com.aksoyhasan.taskyb.repository.MainRepository
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TaskYB)
        setContentView(R.layout.activity_main)

        val mainRepository = MainRepository()
        val viewModelProviderFactory = MainViewModelProviderFactory(application, mainRepository)

        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(MainViewModel::class.java)
        ac_ma_bottomNavigationView.setupWithNavController(ac_ma_nav_host_fragment.findNavController())
    }

    fun showBottomNavigationView() {
        overridePendingTransition(R.anim.bottom_up, R.anim.top_down)
        ac_ma_bottomNavigationView.visibility = View.VISIBLE
    }

    fun hideBottomNavigationView() {
        overridePendingTransition(R.anim.top_down, R.anim.bottom_up)
        ac_ma_bottomNavigationView.visibility = View.GONE
    }

    fun showLoading() {
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        ac_ma_loader_layout.visibility = View.VISIBLE
    }

    fun hideLoading() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        ac_ma_loader_layout.visibility = View.GONE
    }
}