package com.example.myapplication.Fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.LinearLayout.VERTICAL
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Objects.Items
import com.example.myapplication.Objects.RecyclerView_Adapter
import com.example.myapplication.Objects.StoreItemsListAdapter

import com.example.myapplication.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_buy.*
import kotlinx.android.synthetic.main.fragment_buy_fragment_with_recycler_view.*

/**
 * A simple [Fragment] subclass.
 */
private const val MYTAG = "myFragWithRecycle"
class BuyFragmentWithRecyclerView : Fragment() {

    lateinit var ref: DatabaseReference
    lateinit var storeItemsList: MutableList<Items>


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


        storeItemsList = mutableListOf()
        ref = FirebaseDatabase.getInstance().getReference("/mainShop")



//        ref.addValueEventListener(object: ValueEventListener {
//            override fun onCancelled(p0: DatabaseError) {
//                Toast.makeText(requireContext(),p0.message,Toast.LENGTH_LONG).show()
//            }
//
//            override fun onDataChange(mainShop: DataSnapshot) {
//                if(mainShop.exists()){
//                    storeItemsList.clear()
//                    for(items in mainShop.children){
//                        val thisItem = items.getValue(Items::class.java)
//                        storeItemsList.add(thisItem!!)
//                    }
//                    val myAdapter = RecyclerView_Adapter(storeItemsList,requireContext(),savedInstanceState)
//                    recycler_view_id.adapter = myAdapter
//                }
//            }
//
//        })
    }
}
