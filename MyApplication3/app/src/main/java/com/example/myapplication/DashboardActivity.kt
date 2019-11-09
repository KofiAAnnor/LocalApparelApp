package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {

    private val monNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        //This is a view that I added to my DashboardAct. I represents my Bottom Navigation Bar

        //and onNavigationItemSelectedListener is the listener for that view.
        when(item.itemId){
            R.id.UserAID -> {
                println("home pressed")
                Toast.makeText(applicationContext, "Let's Go Home", Toast.LENGTH_LONG).show()
                replaceFragment(User())
                return@OnNavigationItemSelectedListener true
            }
            R.id.Buy_ItemsAID -> {
                println("Buy pressed")
                Toast.makeText(applicationContext, "What would you like to buy", Toast.LENGTH_LONG).show()
                replaceFragment(BuyFrag())
                return@OnNavigationItemSelectedListener true
            }
            R.id.Sell_ClothesAID -> {
                println("Sell pressed")
                Toast.makeText(applicationContext, "Take A Pic Of What You Want To Send", Toast.LENGTH_LONG).show()
                replaceFragment(CameraFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.ChatAID -> {
                println("Chat pressed")
                Toast.makeText(applicationContext, "Let's Talk To Some Sellers", Toast.LENGTH_LONG).show()
                replaceFragment(ChatFragment())
                return@OnNavigationItemSelectedListener true
            }
        }

        false

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        Bottom_NavigationAID.setOnNavigationItemSelectedListener(monNavigationItemSelectedListener)

    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }
}
