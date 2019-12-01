package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_dashboard.*

private const val MYTAG = "myDashAct"
class Dashboard : AppCompatActivity() {

    private lateinit var mPrefs: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        verifyUserIsLoggedIn()

        val navController = Navigation.findNavController(this,R.id.my_nav_host_fragment_id)
        setUpBottomNavMenu(navController)

        mPrefs = getSharedPreferences("MY_SHARED_PREFERENCES", Context.MODE_PRIVATE)
        val email = mPrefs.getString("EMAIL","empty")
        //val id = mPrefs.getString("USERID","NO ID HERE")
        Log.i(MYTAG,"The email is here "+email)
        //Log.i("MYTAG","The ID is here "+email)
    }

    private fun verifyUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if(uid == null){
            //when uid is null it means that the user hasnt logged in yet.
            val mainActIntent = Intent(this,MainActivity::class.java)
            mainActIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(mainActIntent)
        }
    }

    private fun setUpBottomNavMenu(navController: NavController){

        my_bottom_navigaiton_bar_id?.let {
            NavigationUI.setupWithNavController(it,navController)
        }
    }

}
