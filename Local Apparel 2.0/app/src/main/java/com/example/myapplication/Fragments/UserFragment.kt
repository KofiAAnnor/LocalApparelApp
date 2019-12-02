package com.example.myapplication.Fragments


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.myapplication.*
import com.example.myapplication.Objects.User
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_user.*

/**
 * A simple [Fragment] subclass.
 */
class UserFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: FirebaseDatabase
    private lateinit var mDatabaseReference: DatabaseReference
    private val MYTAG = "MyUserFrag"
    var theUrl: String =  ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragPref = this.activity!!.getSharedPreferences("MY_SHARED_PREFERENCES", Context.MODE_PRIVATE)
        val userEmail = fragPref.getString("EMAIL","Loser").toString()
        Log.i(MYTAG,"kofi said the user email is: "+userEmail)
        hello_textview_id.text = "Hello "+fragPref.getString("EMAIL","Loser")


        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference!!.child("users")

        mDatabaseReference.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                Log.i(MYTAG,p0.message)
            }

            val theUserID = mAuth!!.currentUser!!.uid

            override fun onDataChange(p0: DataSnapshot) {
                val currUser = p0.child(theUserID).getValue(User::class.java)
                Log.i(MYTAG,"USER HAS BEEN ACCESSED IN DATABASE!")

                val name = currUser!!.userName
                val imageURL = currUser!!.userImageUrl

                //imageView_id
                hello_textview_id.text = "Hello "+name
                Picasso.get().load(imageURL).into(imageView_id)
            }

        })



        user_logout_button_id.setOnClickListener {

            val mainAct = Intent(this.activity, MainActivity::class.java)
            mainAct.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            FirebaseAuth.getInstance().signOut()
            startActivity(mainAct)
        }

        user_ItemsUpForSale_Button_id.setOnClickListener {
            Log.i(MYTAG,"You clicked the IUFS Button")
            startActivity(Intent(this.activity, MyItemsUpForSaleActivity::class.java))
        }
    }

//    private fun findUserInformationInDatabase(email: String) {
//        val ref = FirebaseDatabase.getInstance().reference
//        val query = ref.child("users")
//
//        Log.i(MYTAG,"YOU ARE LOOKING AT A SNAPSHOT 0")
//        query.addValueEventListener(object: ValueEventListener {
//            override fun onCancelled(p0: DatabaseError) {
//                // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//
//            override fun onDataChange(fbUserList: DataSnapshot) {
//                if(fbUserList.exists()){
//                    Log.i(MYTAG,"YOU ARE LOOKING AT A SNAPSHOT 0")
//                    for(person in fbUserList.children){
//                        Log.i(MYTAG,"YOU ARE LOOKING AT A SNAPSHOT 1") //It doesn't go past 1
//                        val thisUser = person.getValue(User::class.java)
//                        Log.i(MYTAG,"YOU ARE LOOKING AT A SNAPSHOT 2")
//                        if(thisUser!!.userEmail == email){
//                            Log.i(MYTAG,"WE FOUND THE USER")
//                            theUrl = thisUser.userImageUrl!!
//                            return
//                        }
//                    }
//                }
//            }
//        })
//    }

}
