package com.example.projectk

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.net.toUri
import com.example.projectk.activity.ProductDetailActivity
import com.example.projectk.databinding.ActivityDetailBinding
import com.example.projectk.fragment.HomeFragment
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var result = intent.getStringExtra("ResultName")
        var confidence = intent.getFloatExtra("confidence",0.0F)
        var uri = intent.getStringExtra("uri")
        var bundle = intent.getBundleExtra("bundle")
        if (uri != null) {
            de.setImageURI(uri.toUri())
        }
        if(bundle!=null){
            val bitmap = bundle.get("data") as Bitmap
            de.setImageBitmap(bitmap)
        }
        binding.label.text = result
        binding.confidency.text = confidence.toString()

        binding.btnrescan.setOnClickListener{
            val intent = Intent(this,Diagnose::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnHome.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

}