package com.example.projectk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.PopupMenu
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import com.example.projectk.activity.LoginActivity
import com.example.projectk.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var i = 0
   override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       binding = ActivityMainBinding.inflate(layoutInflater)
       setContentView(binding.root)

       if(FirebaseAuth.getInstance().currentUser == null){
           startActivity(Intent(this, LoginActivity::class.java))
           finish()
       }

       val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
       val navController = navHostFragment!!.findNavController()

       val popupMenu = PopupMenu(this, null)
       popupMenu.inflate(R.menu.bottom_navi)
       binding.bottomBar.setupWithNavController(popupMenu.menu, navController)

       navController.addOnDestinationChangedListener { controller, destination, arguments ->
           title = when (destination.id) {
               R.id.cartFragment -> "My Cart"
               R.id.moreFragment -> "Profile"
               R.id.shopFragment -> "Shop"
               else -> "Kreate"
           }
       }

       binding.bottomBar.onItemSelected = {
           when (it) {
               0 -> {
                   i = 0;
                   navController.navigate(R.id.homeFragment)
               }
               1 -> i = 1
               2 -> i = 2
               3 -> i = 3
               4 -> i = 4

           }
       }

   }

    override fun onBackPressed() {
        super.onBackPressed()
        if (i==0){
            finish()
        }

    }

}