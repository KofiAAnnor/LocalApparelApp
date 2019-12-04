package com.example.myapplication.Fragments


import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Objects.Items
import com.example.myapplication.Objects.RecyclerViewAdapter
import com.example.myapplication.Objects.StoreItemsListAdapter

import com.example.myapplication.R
import com.google.firebase.database.*
//import kotlinx.android.synthetic.main.fragment_buy.*
import kotlinx.android.synthetic.main.fragment_buy_fragment_with_recycler_view.*
import java.lang.Integer.parseInt

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

        buyAgain_filterButton_id.setOnClickListener {
            showFilterDialog(savedInstanceState)
        }

        storeItemsList = mutableListOf()
        ref = FirebaseDatabase.getInstance().getReference("/mainShop")

        var mPrefs = getActivity()!!.getSharedPreferences("MY_SHARED_PREFERENCES", Context.MODE_PRIVATE)
        var email = mPrefs.getString("EMAIL", "DEFAULT")

        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(requireContext(),p0.message,Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(mainShop: DataSnapshot) {
                if(mainShop.exists()){
                    storeItemsList.clear()
                    for(items in mainShop.children){
                        val thisItem = items.getValue(Items::class.java)
                        var itememail = thisItem!!.itemEmail

                        if (itememail!=email){
                            storeItemsList.add(thisItem!!)
                        }
                    }
                    storeItemsList.reverse()
                    val myAdapter = RecyclerViewAdapter(storeItemsList,requireContext(),savedInstanceState)
                    recycler_view_id.adapter = myAdapter
                }
            }

        })
    }

    private fun showFilterDialog(savedInstanceState: Bundle?) {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = LayoutInflater.from(requireContext())
        val filterView = inflater.inflate(R.layout.filter_dialog_layout,null)
        builder.setView(filterView)
        val alert = builder.create()
        val minTV = filterView.findViewById<EditText>(R.id.filter_min_ETV_id)
        val maxTV = filterView.findViewById<EditText>(R.id.filter_max_ETV_id)
        val catSpinner = filterView.findViewById<Spinner>(R.id.filter_category_spinner_id)
        val sizeSpinner = filterView.findViewById<Spinner>(R.id.filter_size_spinner_id)
        val filter = filterView.findViewById<Button>(R.id.filter_Accept_Button_id)
        val cancel = filterView.findViewById<Button>(R.id.filter_Cancel_Button_id)


        filter.setOnClickListener {
            var min_price_filter: Int
            var max_price_filter: Int

            if(minTV.text.toString() != "" && maxTV.text.toString() == "" ){
                min_price_filter = minTV.text.toString().toInt()
                max_price_filter = Int.MAX_VALUE
            }else if (minTV.text.toString() == "" && maxTV.text.toString() != "" ){
                min_price_filter = 0
                max_price_filter = maxTV.text.toString().toInt()
            }else if (minTV.text.toString() == "" && maxTV.text.toString() == "" ){
                min_price_filter = 0
                max_price_filter = Int.MAX_VALUE
            }else {
                min_price_filter = minTV.text.toString().toInt()
                max_price_filter = maxTV.text.toString().toInt()
            }

            var category = catSpinner.selectedItem.toString()
            var size = sizeSpinner.selectedItem.toString()
            var mPrefs = getActivity()!!.getSharedPreferences("MY_SHARED_PREFERENCES", Context.MODE_PRIVATE)
            var email = mPrefs.getString("EMAIL", "DEFAULT")

            Log.i(MYTAG,"MIN SEARCH PRICE: $min_price_filter")
            Log.i(MYTAG,"MAX SEARCH PRICE: $max_price_filter")
            Log.i(MYTAG,"SEARCH CATEGORY: $category")
            Log.i(MYTAG,"SEARCH SIZE: $size")
            Log.i(MYTAG,"SEARCH CURRENT EMAIL: $email")

            ref = FirebaseDatabase.getInstance().reference
            val filterQuery = ref.child("mainShop")

            filterQuery.addValueEventListener(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(mainShop: DataSnapshot) {
                    if(mainShop.exists()){
                        storeItemsList.clear()
                        for(items in mainShop.children) {
                            val thisItem = items.getValue(Items::class.java)
                            var price = thisItem!!.itemPrice!!.toInt()
                            var itemsize = thisItem!!.itemSize
                            var itemcategory = thisItem!!.itemCategory
                            var itememail = thisItem!!.itemEmail
                            if(category=="CATEGORY"){
                                itemcategory = "CATEGORY"
                            }
                            if(size=="SIZE"){
                                itemsize = "SIZE"
                            }
                            if (price in min_price_filter..max_price_filter && size == itemsize && itemcategory == category&&itememail!=email){
                                storeItemsList.add(thisItem!!)
                            }
                        }
                        storeItemsList.reverse()
                        val myAdapter = RecyclerViewAdapter(storeItemsList,requireContext(),savedInstanceState)
                        recycler_view_id.adapter = myAdapter
                    }
                }

            })
            alert.dismiss()
        }

        cancel.setOnClickListener {
            alert.dismiss()
        }

        alert.show()
    }
}