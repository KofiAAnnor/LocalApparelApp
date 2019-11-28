package com.example.myapplication.Objects

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.myapplication.R

class MyItemsSoldListViewAdapter(val mCtx: Context, val layoutResID: Int, val itemsList: List<Items>):
    ArrayAdapter<Items>(mCtx,layoutResID,itemsList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //return super.getView(position, convertView, parent)
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val myView: View = layoutInflater.inflate(layoutResID,null)

        val textViewName = myView.findViewById<TextView>(R.id.itemName_id)
        val item = itemsList[position]
        textViewName.text = item.itemName
        return myView
    }
}