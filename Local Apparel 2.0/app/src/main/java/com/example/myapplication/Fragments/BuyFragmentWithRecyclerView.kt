package com.example.myapplication.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.LinearLayout.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Objects.Items

import com.example.myapplication.R
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_buy_fragment_with_recycler_view.*

/**
 * A simple [Fragment] subclass.
 */
class BuyFragmentWithRecyclerView : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buy_fragment_with_recycler_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view_id.layoutManager=LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)

        val items = ArrayList<Items>()


    }
}
