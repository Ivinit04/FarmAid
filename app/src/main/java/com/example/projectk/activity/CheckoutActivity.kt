package com.example.projectk.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.projectk.MainActivity
import com.example.projectk.databinding.ActivityCheckoutBinding
import com.example.projectk.roomdb.AppDatabase
import com.example.projectk.roomdb.ProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import org.json.JSONObject


class CheckoutActivity : AppCompatActivity(), PaymentResultListener {

    private lateinit var binding : ActivityCheckoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)

        setContentView(binding.root)
        val checkout = Checkout()
        checkout.setKeyID("rzp_test_VvbsSqFi5HW5aq");

        val price = intent.getStringExtra("totalCost")

        try {
            val options = JSONObject()
            options.put("name","FarmAid Corp")
            options.put("description","Product Charges")
            //You can omit the image option to fetch the image from the dashboard
            options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
            options.put("theme.color", "#52B788")
            options.put("currency","INR")
            options.put("amount",(price!!.toInt()*100))

            options.put("email","rushabhbarde@gmail.com")
            options.put("contact","8999089753")

            checkout.open(this, options)
        }catch (e: Exception){
            Toast.makeText(this,"Error in payment!",Toast.LENGTH_SHORT).show()
        }

    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this,"Payment Success",Toast.LENGTH_SHORT).show()
        
        uploadData()
        
    }

    private fun uploadData() {
        val id = intent.getStringArrayListExtra("productIds")
        for(currentId in id!!){
            fetchData(currentId)
        }
    }

    private fun fetchData(productId: String?) {

        val dao = AppDatabase.getInstance(this).productDao()

        Firebase.firestore.collection("products")
            .document(productId!!).get().addOnSuccessListener{

                lifecycleScope.launch(Dispatchers.IO){
                    dao.deleteProduct(ProductModel(productId))
                }

                saveData(it.getString("productName"),
                    it.getString("productSp"),
                    productId)

            }
    }

    private fun saveData(name: String?, price: String?, productId: String) {

        val preference = this.getSharedPreferences("user", MODE_PRIVATE)
        val data = hashMapOf<String, Any>()
        data["name"] = name!!
        data["price"] = price!!
        data["productId"] = productId
        data["status"] = "Ordered"
        data["userId"] = preference.getString("number", " ")!!

        val firestore = Firebase.firestore.collection("allOrders")
        val key = firestore.document().id
        data["orderId"] = key

        firestore.document(key).set(data).addOnSuccessListener {
            Toast.makeText(this, "Ordered Placed!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }.addOnFailureListener{
            Toast.makeText(this, "Something went Wrong!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this,"Payment Error!",Toast.LENGTH_SHORT).show()
    }
}