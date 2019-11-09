package com.example.myapplication


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent


/**
 * A simple [Fragment] subclass.
 */
class CameraFragment : Fragment() {

    private lateinit var camBtn: Button

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {
        // Inflate the layout for this fragment
        //initializeUI()
        //camBtn.setOnClickListener { camBtnListener() }
        return inflater.inflate(R.layout.fragment_camera, container, false)

    }

    private fun initializeUI() {
        camBtn = activity!!.findViewById(R.id.cameraBtn)

    }

    private fun camBtnListener() {
        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        startActivity(intent)
    }

}
