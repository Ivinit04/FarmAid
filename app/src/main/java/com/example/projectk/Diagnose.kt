package com.example.projectk

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media
import android.view.Gravity
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.projectk.databinding.ActivityDiagnoseBinding
import kotlinx.android.synthetic.main.activity_diagnose.*
import java.io.IOException

class Diagnose : AppCompatActivity() {

    private  var count: Int = 0
    private lateinit var binding: ActivityDiagnoseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiagnoseBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        val intent = getIntent()
        binding.cotton.setOnClickListener {
            count = 1
            val intent = Intent(this, DiagnoseCamera::class.java)
//            intent.putExtra("Cotton", "cotton")
            intent.putExtra("Count",count)
            startActivity(intent)
        }
        binding.corn.setOnClickListener {
            count = 2
            val intent = Intent(this, DiagnoseCamera::class.java)
//            intent.putExtra("Corn", "corn")
            intent.putExtra("Count",count)
            startActivity(intent)
        }
        binding.potato.setOnClickListener {
            count = 3
            val intent = Intent(this, DiagnoseCamera::class.java)
//            intent.putExtra("Potato", "potato")
            intent.putExtra("Count",count)
            startActivity(intent)
        }
        binding.grape.setOnClickListener {
            count = 4
            val intent = Intent(this, DiagnoseCamera::class.java)
//            intent.putExtra("Grape", "grape")
            intent.putExtra("Count",count)
            startActivity(intent)
        }
        binding.banana.setOnClickListener {
            count = 5
            val intent = Intent(this, DiagnoseCamera::class.java)
//            intent.putExtra("Tomato", "tomato")
            intent.putExtra("Count",count)
            startActivity(intent)
        }
        binding.bell.setOnClickListener {
            count = 6
            val intent = Intent(this, DiagnoseCamera::class.java)
//            intent.putExtra("Bell", "bell")
            intent.putExtra("Count",count)
            startActivity(intent)
        }

        binding.apple.setOnClickListener {
            count = 7
            val intent = Intent(this, DiagnoseCamera::class.java)
//            intent.putExtra("Apple", "apple")
            intent.putExtra("Count",count)
            startActivity(intent)
        }

        binding.wheat.setOnClickListener {
            count = 8
            val intent = Intent(this, DiagnoseCamera::class.java)
//            intent.putExtra("Wheat", "wheat")
            intent.putExtra("Count",count)
            startActivity(intent)
        }

        binding.sugarcane.setOnClickListener {
            count = 9
            val intent = Intent(this, DiagnoseCamera::class.java)
//            intent.putExtra("Sugarcane", "sugarcane")
            intent.putExtra("Count",count)
            startActivity(intent)
        }

        binding.chili.setOnClickListener {
            count = 10
            val intent = Intent(this, DiagnoseCamera::class.java)
//            intent.putExtra("Chili", "chili")
            intent.putExtra("Count",count)
            startActivity(intent)
        }

//       storage.setOnClickListener {
//    val intent=Intent(Intent.ACTION_PICK)
//    intent.type="image/*"
//    startActivityForResult(intent,0)
//}
//
//    }
//    var selected: Uri?=null
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(requestCode==0&&resultCode== Activity.RESULT_OK&&data!=null){
//            selected=data.data
//            val bitmap= MediaStore.Images.Media.getBitmap(contentResolver,selected)
//            cicle_image.setImageBitmap(bitmap)
//            select.alpha=0F
//        }
//    }

    }
}