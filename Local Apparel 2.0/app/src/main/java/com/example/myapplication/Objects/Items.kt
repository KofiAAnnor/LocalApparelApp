package com.example.myapplication.Objects

class Items {

    var itemID: String? = null
    var itemName: String? = null
    var itemSize: String? = null
    var itemPrice: String? = null
    var itemBrand: String? = null
    var itemCondition: String? = null
    var itemCategory: String? = null
    var itemDescription: String? = null

    constructor(name:String, size:String, price:String, brand:String,itemCondition: String,
                itemCategory: String, itemDescription: String){
        this.itemName = name
        this.itemSize = size
        this.itemPrice = price
        this.itemBrand = brand
        this.itemCondition = itemCondition
        this.itemCategory = itemCategory
        this.itemDescription = itemDescription
    }

    fun setID(id: String){
        this.itemID = id
    }
    fun setName(name: String){
        this.itemName = name
    }

    fun setBrand(brand: String){
        this.itemBrand = brand
    }


    constructor(name:String, size:String, price:String){
        this.itemName = name
        this.itemSize = size
        this.itemPrice = price
    }

    constructor(): this("","","","","","","")
}