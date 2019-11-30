package com.example.myapplication.Fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.Objects.Items
import com.example.myapplication.Objects.MyItemsUpForSaleListAdapter
import com.example.myapplication.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_items_up_for_sale.*

/**
 * A simple [Fragment] subclass.
 */
class BuyFragment : Fragment() {

    lateinit var ref: DatabaseReference
    lateinit var storeItemsList: MutableList<Items>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storeItemsList = mutableListOf()
        ref = FirebaseDatabase.getInstance().getReference("mainShop")

        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                Log.i("myBuyFrag","You are looking at snapshots")
                if(p0.exists()){
                    storeItemsList.clear()
                    for(h in p0.children){
                        Log.i("myBuyFrag","You are looking at snapshots AGAIN!")
                        val thisItem = h.getValue(Items::class.java)
                        storeItemsList.add(thisItem!!)
                    }

                    val myAdapter = MyItemsUpForSaleListAdapter(requireContext(),R.layout.store_items_layout,storeItemsList)
                    //Todo change to = storeItemsAdapter
                    my_IUFS_listView_id.adapter = myAdapter
                }
            }

        })



    }

}
