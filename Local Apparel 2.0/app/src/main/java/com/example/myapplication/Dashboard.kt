package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_dashboard.*

class Dashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val navController = Navigation.findNavController(this,R.id.my_nav_host_fragment_id)
        setUpBottomNavMenu(navController)


    }


    private fun setUpBottomNavMenu(navController: NavController){
        my_bottom_navigaiton_bar_id?.let {
            NavigationUI.setupWithNavController(it,navController)
        }
    }

}
