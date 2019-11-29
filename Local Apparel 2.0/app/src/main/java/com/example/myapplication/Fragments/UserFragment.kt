package com.example.myapplication.Fragments


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.myapplication.*
import com.example.myapplication.R
import kotlinx.android.synthetic.main.fragment_user.*

/**
 * A simple [Fragment] subclass.
 */
class UserFragment : Fragment() {
    private val MYTAG = "MyUserFrag"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            hello_textview_id.text = "Nothing Saved"
        } else {
            hello_textview_id.text = "It's not null"
        }

        user_logout_button_id.setOnClickListener {
            startActivity(Intent(this.activity, MainActivity::class.java))
        }

        user_ItemsUpForSale_Button_id.setOnClickListener {
            Log.i("MYTAG","You clicked the IUFS Button")
            startActivity(Intent(this.activity, MyItemsUpForSaleActivity::class.java))
        }

        user_wishList_button_id.setOnClickListener {
            Toast.makeText(activity,"Not Implemented",Toast.LENGTH_LONG).show()
        }

    }

}
