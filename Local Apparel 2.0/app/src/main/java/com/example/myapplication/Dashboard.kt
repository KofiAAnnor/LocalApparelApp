package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.activity_dashboard.*


class Dashboard : AppCompatActivity() {

    private lateinit var mPrefs: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val navController = Navigation.findNavController(this,R.id.my_nav_host_fragment_id)
        setUpBottomNavMenu(navController)

        mPrefs = getSharedPreferences("MY_SHARED_PREFERENCES", Context.MODE_PRIVATE)
        val email = mPrefs.getString("EMAIL","empty")
        Log.i("MYTAG","The email is here "+email)
    }

    private fun setUpBottomNavMenu(navController: NavController){

        my_bottom_navigaiton_bar_id?.let {
            NavigationUI.setupWithNavController(it,navController)
        }
    }

}
