package com.example.projectk

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.projectk.databinding.ActivityDiagnoseCameraBinding
import com.example.projectk.ml.AppleDisease
import com.example.projectk.ml.BananaDisease
import com.example.projectk.ml.ChiliDisease
import com.example.projectk.ml.CornDisease
import com.example.projectk.ml.CottonDisease
import com.example.projectk.ml.GrapeDisease
import com.example.projectk.ml.PepperDisease
import com.example.projectk.ml.PotatoDisease
import com.example.projectk.ml.SugarcaneDisease
import com.example.projectk.ml.WheatDisease
import kotlinx.android.synthetic.main.activity_diagnose.*
import kotlinx.android.synthetic.main.activity_diagnose_camera.*
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

class DiagnoseCamera : AppCompatActivity() {

    lateinit var bitmap: Bitmap
    private lateinit var binding: ActivityDiagnoseCameraBinding
    private lateinit var result: String
    private var confidency: Float = 0.0F
    private  var bundle: Bundle? = null
    private  var uri: Uri? = null
//    private var mCurrentPhotoPath: String? = null;



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDiagnoseCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val count = intent.getIntExtra("Count",1)
        binding.predict.isEnabled = false

        if(count==1){
            binding.scan.text="Scan the leaf of Cotton"
        }
        if(count==2){
            binding.scan.text="Scan the leaf of Corn"
        }
        if(count==3){
            binding.scan.text="Scan the leaf of Potato"
        }
        if(count==4){
            binding.scan.text="Scan the leaf of Grape"
        }
        if(count==5){
            binding.scan.text="Scan the leaf of Banana"
        }
        if(count==6){
            binding.scan.text="Scan the leaf of Bell"
        }
        if(count==7){
            binding.scan.text="Scan the leaf of Apple"
        }
        if(count==8){
            binding.scan.text="Scan the leaf of Wheat"
        }
        if(count==9){
            binding.scan.text="Scan the leaf of Sugarcane"
        }
        if(count==10){
            binding.scan.text="Scan the leaf of Chili"
        }

        predict.setOnClickListener {
            if(count==1){
                predictCotton()
            }
            if(count==2){
                predictCorn()
            }
            if(count==3){
                predictPotato()
            }
            if(count==4){
                predictGrape()
            }
            if(count==5){
                predictBanana()
            }
            if(count==6){
                predictBell()
            }
            if(count==7){
                predictApple()
            }
            if(count==8){
                predictWheat()
            }
            if(count==9){
                predictSugarcane()
            }
            if(count==10){
                predictChili()
            }

            val intent = Intent(this,DetailActivity::class.java)
            intent.putExtra("ResultName",result)
            intent.putExtra("uri",uri.toString())
            intent.putExtra("bundle",bundle)
            intent.putExtra("confidence",confidency)
            startActivity(intent)
        }

        binding.galleryImg.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    105
                )
            }else {
                val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"

                startActivityForResult(intent, 100)
                binding.predict.isEnabled = true
            }
        }

        binding.cameraImg.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!=
                PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),
                    200)
            }else{
                //code for camera
                openCameraForResult(110)
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==100 && resultCode== RESULT_OK) {

            binding.leafImage.setImageURI(data?.data)
            uri = data?.data
            bundle = null
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
        }else if(requestCode ==110 && resultCode== RESULT_OK){
            uri = null
            bundle = data?.extras
            bitmap = data?.extras?.get("data") as Bitmap
            binding.leafImage.setImageBitmap(bitmap)
        }
    }
    private fun predictPotato() {
        var image: Bitmap = Bitmap.createScaledBitmap(bitmap,256,256,true)
        val model = PotatoDisease.newInstance(this)

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 256, 256, 3), DataType.FLOAT32)

        val byteBuffer: ByteBuffer = ByteBuffer.allocateDirect(4 * 256 * 256 * 3)
        byteBuffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(256 * 256)
        image.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)
        var pixel = 0

        for (i in 0 until 256) {
            for (j in 0 until 256) {
                val `val` = intValues[pixel++] // RGB
                byteBuffer.putFloat((`val` shr 16 and 0xFF) * (1f / 1))
                byteBuffer.putFloat((`val` shr 8 and 0xFF) * (1f / 1))
                byteBuffer.putFloat((`val` and 0xFF) * (1f / 1))
            }
        }

        inputFeature0.loadBuffer(byteBuffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val confidences = outputFeature0.floatArray

        var maxPos = 0
        var maxConfidence = 0f
        for (i in confidences.indices) {
            if (confidences[i] > maxConfidence) {
                maxConfidence = confidences[i]
                maxPos = i
            }
        }
        val classes = arrayOf("Potato__Early_blight", "Potato_Late_blight", "Potato__healthy")
        confidency=maxConfidence
        result = classes[maxPos]

        model.close()
    }



    private fun predictCotton() {
        var image: Bitmap = Bitmap.createScaledBitmap(bitmap,256,256,true)
        val model = CottonDisease.newInstance(this)

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 256, 256, 3), DataType.FLOAT32)

        val byteBuffer: ByteBuffer = ByteBuffer.allocateDirect(4 * 256 * 256 * 3)
        byteBuffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(256 * 256)
        image.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)
        var pixel = 0

        for (i in 0 until 256) {
            for (j in 0 until 256) {
                val `val` = intValues[pixel++] // RGB
                byteBuffer.putFloat((`val` shr 16 and 0xFF) * (1f / 1))
                byteBuffer.putFloat((`val` shr 8 and 0xFF) * (1f / 1))
                byteBuffer.putFloat((`val` and 0xFF) * (1f / 1))
            }
        }

        inputFeature0.loadBuffer(byteBuffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val confidences = outputFeature0.floatArray

        var maxPos = 0
        var maxConfidence = 0f
        for (i in confidences.indices) {
            if (confidences[i] > maxConfidence) {
                maxConfidence = confidences[i]
                maxPos = i
            }
        }
        val classes = arrayOf("Aphids", "Army worm", "Bacterial Blight","Healthy","Powdery Mildew","Target spot")
        confidency=maxConfidence
        result = classes[maxPos]

        model.close()
    }

    private fun predictApple() {
        var image: Bitmap = Bitmap.createScaledBitmap(bitmap,256,256,true)
        val model = AppleDisease.newInstance(this)

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 256, 256, 3), DataType.FLOAT32)

        val byteBuffer: ByteBuffer = ByteBuffer.allocateDirect(4 * 256 * 256 * 3)
        byteBuffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(256 * 256)
        image.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)
        var pixel = 0

        for (i in 0 until 256) {
            for (j in 0 until 256) {
                val `val` = intValues[pixel++] // RGB
                byteBuffer.putFloat((`val` shr 16 and 0xFF) * (1f / 1))
                byteBuffer.putFloat((`val` shr 8 and 0xFF) * (1f / 1))
                byteBuffer.putFloat((`val` and 0xFF) * (1f / 1))
            }
        }

        inputFeature0.loadBuffer(byteBuffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val confidences = outputFeature0.floatArray

        var maxPos = 0
        var maxConfidence = 0f
        for (i in confidences.indices) {
            if (confidences[i] > maxConfidence) {
                maxConfidence = confidences[i]
                maxPos = i
            }
        }
        val classes = arrayOf("Apple Black rot", "Apple Healthy", "Apple Scab", "Cedar apple rust")
        result = classes[maxPos]
        confidency=maxConfidence

        model.close()
    }

    private fun predictBell() {
        var image: Bitmap = Bitmap.createScaledBitmap(bitmap,256,256,true)
        val model = PepperDisease.newInstance(this)

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 256, 256, 3), DataType.FLOAT32)

        val byteBuffer: ByteBuffer = ByteBuffer.allocateDirect(4 * 256 * 256 * 3)
        byteBuffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(256 * 256)
        image.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)
        var pixel = 0

        for (i in 0 until 256) {
            for (j in 0 until 256) {
                val `val` = intValues[pixel++] // RGB
                byteBuffer.putFloat((`val` shr 16 and 0xFF) * (1f / 1))
                byteBuffer.putFloat((`val` shr 8 and 0xFF) * (1f / 1))
                byteBuffer.putFloat((`val` and 0xFF) * (1f / 1))
            }
        }

        inputFeature0.loadBuffer(byteBuffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val confidences = outputFeature0.floatArray

        var maxPos = 0
        var maxConfidence = 0f
        for (i in confidences.indices) {
            if (confidences[i] > maxConfidence) {
                maxConfidence = confidences[i]
                maxPos = i
            }
        }
        val classes = arrayOf("Pepper","bell_Bacterial_spot","Pepper,_bell__healthy")
        result = classes[maxPos]
        confidency=maxConfidence

        model.close()
    }

    private fun predictBanana() {
        var image: Bitmap = Bitmap.createScaledBitmap(bitmap,256,256,true)
        val model = BananaDisease.newInstance(this)

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 256, 256, 3), DataType.FLOAT32)

        val byteBuffer: ByteBuffer = ByteBuffer.allocateDirect(4 * 256 * 256 * 3)
        byteBuffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(256 * 256)
        image.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)
        var pixel = 0

        for (i in 0 until 256) {
            for (j in 0 until 256) {
                val `val` = intValues[pixel++] // RGB
                byteBuffer.putFloat((`val` shr 16 and 0xFF) * (1f / 1))
                byteBuffer.putFloat((`val` shr 8 and 0xFF) * (1f / 1))
                byteBuffer.putFloat((`val` and 0xFF) * (1f / 1))
            }
        }

        inputFeature0.loadBuffer(byteBuffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val confidences = outputFeature0.floatArray

        var maxPos = 0
        var maxConfidence = 0f
        for (i in confidences.indices) {
            if (confidences[i] > maxConfidence) {
                maxConfidence = confidences[i]
                maxPos = i
            }
        }
        val classes = arrayOf("cordana","healthy","pestalotiopsis","sigatoka")
        result = classes[maxPos]
        confidency=maxConfidence

        model.close()
    }

    private fun predictGrape() {
        var image: Bitmap = Bitmap.createScaledBitmap(bitmap,256,256,true)
        val model = GrapeDisease.newInstance(this)

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 256, 256, 3), DataType.FLOAT32)

        val byteBuffer: ByteBuffer = ByteBuffer.allocateDirect(4 * 256 * 256 * 3)
        byteBuffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(256 * 256)
        image.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)
        var pixel = 0

        for (i in 0 until 256) {
            for (j in 0 until 256) {
                val `val` = intValues[pixel++] // RGB
                byteBuffer.putFloat((`val` shr 16 and 0xFF) * (1f / 1))
                byteBuffer.putFloat((`val` shr 8 and 0xFF) * (1f / 1))
                byteBuffer.putFloat((`val` and 0xFF) * (1f / 1))
            }
        }

        inputFeature0.loadBuffer(byteBuffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val confidences = outputFeature0.floatArray

        var maxPos = 0
        var maxConfidence = 0f
        for (i in confidences.indices) {
            if (confidences[i] > maxConfidence) {
                maxConfidence = confidences[i]
                maxPos = i
            }
        }
        val classes = arrayOf("Grape___Black_rot",
            "Grape__Esca(Black_Measles)",
            "Grape__Leaf_blight(Isariopsis_Leaf_Spot)",
            "Grape___healthy")
        result = classes[maxPos]
        confidency=maxConfidence

        model.close()
    }

    private fun predictCorn() {
        var image: Bitmap = Bitmap.createScaledBitmap(bitmap,256,256,true)
        val model = CornDisease.newInstance(this)

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 256, 256, 3), DataType.FLOAT32)

        val byteBuffer: ByteBuffer = ByteBuffer.allocateDirect(4 * 256 * 256 * 3)
        byteBuffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(256 * 256)
        image.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)
        var pixel = 0

        for (i in 0 until 256) {
            for (j in 0 until 256) {
                val `val` = intValues[pixel++] // RGB
                byteBuffer.putFloat((`val` shr 16 and 0xFF) * (1f / 1))
                byteBuffer.putFloat((`val` shr 8 and 0xFF) * (1f / 1))
                byteBuffer.putFloat((`val` and 0xFF) * (1f / 1))
            }
        }

        inputFeature0.loadBuffer(byteBuffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val confidences = outputFeature0.floatArray

        var maxPos = 0
        var maxConfidence = 0f
        for (i in confidences.indices) {
            if (confidences[i] > maxConfidence) {
                maxConfidence = confidences[i]
                maxPos = i
            }
        }
        val classes = arrayOf("Blight", "Common_Rust", "Gray_Leaf_Spot", "Healthy")
        result = classes[maxPos]
        confidency=maxConfidence

        model.close()
    }

    private fun predictWheat() {
        var image: Bitmap = Bitmap.createScaledBitmap(bitmap,256,256,true)
        val model = WheatDisease.newInstance(this)

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 256, 256, 3), DataType.FLOAT32)

        val byteBuffer: ByteBuffer = ByteBuffer.allocateDirect(4 * 256 * 256 * 3)
        byteBuffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(256 * 256)
        image.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)
        var pixel = 0

        for (i in 0 until 256) {
            for (j in 0 until 256) {
                val `val` = intValues[pixel++] // RGB
                byteBuffer.putFloat((`val` shr 16 and 0xFF) * (1f / 1))
                byteBuffer.putFloat((`val` shr 8 and 0xFF) * (1f / 1))
                byteBuffer.putFloat((`val` and 0xFF) * (1f / 1))
            }
        }

        inputFeature0.loadBuffer(byteBuffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val confidences = outputFeature0.floatArray

        var maxPos = 0
        var maxConfidence = 0f
        for (i in confidences.indices) {
            if (confidences[i] > maxConfidence) {
                maxConfidence = confidences[i]
                maxPos = i
            }
        }
        val classes = arrayOf("Healthy", "septoria", "stripe_rust")
        confidency=maxConfidence
        result = classes[maxPos]

        model.close()
    }

    private fun predictSugarcane() {
        var image: Bitmap = Bitmap.createScaledBitmap(bitmap,256,256,true)
        val model = SugarcaneDisease.newInstance(this)

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 256, 256, 3), DataType.FLOAT32)

        val byteBuffer: ByteBuffer = ByteBuffer.allocateDirect(4 * 256 * 256 * 3)
        byteBuffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(256 * 256)
        image.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)
        var pixel = 0

        for (i in 0 until 256) {
            for (j in 0 until 256) {
                val `val` = intValues[pixel++] // RGB
                byteBuffer.putFloat((`val` shr 16 and 0xFF) * (1f / 1))
                byteBuffer.putFloat((`val` shr 8 and 0xFF) * (1f / 1))
                byteBuffer.putFloat((`val` and 0xFF) * (1f / 1))
            }
        }

        inputFeature0.loadBuffer(byteBuffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val confidences = outputFeature0.floatArray

        var maxPos = 0
        var maxConfidence = 0f
        for (i in confidences.indices) {
            if (confidences[i] > maxConfidence) {
                maxConfidence = confidences[i]
                maxPos = i
            }
        }
        val classes = arrayOf("Blight", "Healthy", "RedRot", "RedRust")
        confidency=maxConfidence
        result = classes[maxPos]

        model.close()
    }

    private fun predictChili() {
        var image: Bitmap = Bitmap.createScaledBitmap(bitmap,256,256,true)
        val model = ChiliDisease.newInstance(this)

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 256, 256, 3), DataType.FLOAT32)

        val byteBuffer: ByteBuffer = ByteBuffer.allocateDirect(4 * 256 * 256 * 3)
        byteBuffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(256 * 256)
        image.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)
        var pixel = 0

        for (i in 0 until 256) {
            for (j in 0 until 256) {
                val `val` = intValues[pixel++] // RGB
                byteBuffer.putFloat((`val` shr 16 and 0xFF) * (1f / 1))
                byteBuffer.putFloat((`val` shr 8 and 0xFF) * (1f / 1))
                byteBuffer.putFloat((`val` and 0xFF) * (1f / 1))
            }
        }

        inputFeature0.loadBuffer(byteBuffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val confidences = outputFeature0.floatArray

        var maxPos = 0
        var maxConfidence = 0f
        for (i in confidences.indices) {
            if (confidences[i] > maxConfidence) {
                maxConfidence = confidences[i]
                maxPos = i
            }
        }
        val classes = arrayOf("healthy", "leaf curl", "leaf spot", "whitefly","yellowish")
        confidency=maxConfidence
        result = classes[maxPos]

        model.close()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 105) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read external storage permission granted", Toast.LENGTH_SHORT)
                    .show()
                val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"

                startActivityForResult(intent, 100)
                binding.predict.isEnabled = true
            } else {
                Toast.makeText(this, "Read external storage permission denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        if (requestCode == 200) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT)
                    .show()
                openCameraForResult(requestCode)
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }


    private fun openCameraForResult(requestCode: Int) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent,requestCode)

        binding.predict.isEnabled=true
    }
}










//
//    private fun takePicture() {
//
//        val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        val file: File = createFile()
//
//        val uri: Uri = FileProvider.getUriForFile(
//            this,
//            "com.example.android.fileprovider",
//            file
//        )
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
//        startActivityForResult(intent, 101)
//
//    }
//
//
//    fun getMax(arr: FloatArray): Int{
//        var ind = 0
//        var min = 0.0f
//        for( i in 0..1000){
//            if(arr[i]>min)
//                ind = i
//            min = arr[i]
//        }
//        return ind
//    }

//    @Throws(IOException::class)
//    private fun createFile(): File {
//        // Create an image file name
//        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        return File.createTempFile(
//            "JPEG_${timeStamp}_", /* prefix */
//            ".jpg", /* suffix */
//            storageDir /* directory */
//        ).apply {
//            // Save a file: path for use with ACTION_VIEW intents
//            mCurrentPhotoPath = absolutePath
//        }
//    }