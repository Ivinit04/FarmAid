package com.example.projectk.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.projectk.R
import com.example.projectk.databinding.ActivityAddressBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddressActivity : AppCompatActivity() {

    private lateinit var  binding : ActivityAddressBinding
    private lateinit var preferences : SharedPreferences

    private lateinit var totalCost : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)

        setContentView(binding.root)
        preferences = this.getSharedPreferences("user", MODE_PRIVATE)

        totalCost = intent.getStringExtra("totalCost")!!
        loadUserInfo()

        binding.checkoutbtn.setOnClickListener{
            validateData(
                binding.userNumber.text.toString(),
                binding.userName.text.toString(),
                binding.userVillage.text.toString(),
                binding.userCity.text.toString(),
                binding.userState.text.toString(),
                binding.userCode.text.toString()
            )
        }
    }

    private fun validateData(number: String, name: String, village: String, city: String, state: String, pincode: String) {

        if (number.isEmpty() || state.isEmpty() || name.isEmpty()){
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()

        }else{
            storeData(village, city, state, pincode)
        }
    }

    private fun storeData(village: String, city: String, state: String, pincode: String) {
        val map = hashMapOf<String, Any>()
        map["village"] = village
        map["state"] = state
        map["city"] = city
        map["pincode"] = pincode

        Firebase.firestore.collection("users")
            .document(preferences.getString("number", "")!!)
            .update(map).addOnSuccessListener {
                val b = Bundle()
                b.putStringArrayList("productIds",intent.getStringArrayListExtra("productIds"))
                b.putString("totalCost",totalCost)
                val intent = Intent(this, CheckoutActivity::class.java)

                intent.putExtras(b)
                startActivity(intent)
            }.addOnFailureListener{
                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
            }

    }

    private fun loadUserInfo() {



        Firebase.firestore.collection("users").document(preferences.getString("number", "")!!)
            .get().addOnSuccessListener {
                binding.userName.setText(it.getString("userName"))
                binding.userNumber.setText(it.getString("userPhoneNumber"))
                binding.userVillage.setText(it.getString("village"))
                binding.userCity.setText(it.getString("state"))
                binding.userState.setText(it.getString("city"))
                binding.userCode.setText(it.getString("pincode"))
            }.addOnFailureListener{

            }
    }
}