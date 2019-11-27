package com.example.myapplication.Fragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.myapplication.Dashboard
import com.example.myapplication.LoginActivity
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import kotlinx.android.synthetic.main.fragment_user.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class UserFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {



        super.onViewCreated(view, savedInstanceState)
        //IO did the Go to buy button different from all the others just to show that I could do it in more
        //than one way.
        go_to_chat_button_id.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.user_to_chat_action_id))
        go_to_test_button_id.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.user_to_test_action_id))
        go_to_buy_button_id.setOnClickListener{
            Toast.makeText(activity, "We Toastin!!", Toast.LENGTH_SHORT).show()
            it.findNavController().navigate(R.id.user_to_buy_action_id)
        }

        user_logout_button_id.setOnClickListener {
            startActivity(Intent(this.activity, MainActivity::class.java))
        }
    }

}
