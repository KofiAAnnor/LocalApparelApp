package com.example.myapplication.Fragments


import android.app.AlertDialog
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
import kotlinx.android.synthetic.main.fragment_buy.*
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



        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(requireContext(),p0.message,Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(mainShop: DataSnapshot) {
                if(mainShop.exists()){
                    storeItemsList.clear()
                    for(items in mainShop.children){
                        val thisItem = items.getValue(Items::class.java)
                        storeItemsList.add(thisItem!!)
                    }
                    val myAdapter = RecyclerViewAdapter(storeItemsList,requireContext(),savedInstanceState)
                    recycler_view_id.adapter = myAdapter
                }
            }

        })
    }

    private fun showFilterDialog(savedInstanceState: Bundle?) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Select Filters")
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

        val minPrice = minTV.text
        val maxPrice = maxTV.text
        val category = catSpinner.selectedItem.toString()
        val size = sizeSpinner.selectedItem.toString()

        filter.setOnClickListener {
            Log.i(MYTAG,"MIN SEARCH PRICE: $minPrice")
            Log.i(MYTAG,"MAX SEARCH PRICE: $maxPrice")
            Log.i(MYTAG,"SEARCH CATEGORY: $category")
            Log.i(MYTAG,"SEARCH SIZE: $size")
            alert.dismiss()

            //KOFI MAGIC******************

            ref = FirebaseDatabase.getInstance().reference
            val filter_query = ref.child("mainShop")

            filter_query.addValueEventListener(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(mainShop: DataSnapshot) {
                    if(mainShop.exists()){
                        storeItemsList.clear()
                        for(items in mainShop.children){
                            val thisItem = items.getValue(Items::class.java)
                            //val itemName = thisItem!!.itemName.toString()
                            //Log.i(MYTAG, "THE ITEM NAME IS $itemName")
                            val priceQ = thisItem?.itemPrice
                            val priceI = parseInt(priceQ.toString())
                            val priceMin = parseInt(minPrice.toString())
                            val priceMax = parseInt(maxPrice.toString())
                            val sizev = thisItem?.itemSize
                            val categoryv = thisItem?.itemCategory

                            if(priceI in priceMin..priceMax &&sizev.toString()==(size)&&categoryv.toString()==(category)) {
                                storeItemsList.add(thisItem!!)
                            }
                            val myAdapter = RecyclerViewAdapter(storeItemsList,requireContext(),savedInstanceState)
                            recycler_view_id.adapter = myAdapter
                        }
                    }
                }

            })
            //KOFI MAGIC ******************
        }

        cancel.setOnClickListener {
            alert.dismiss()
        }

        alert.show()
    }
}
