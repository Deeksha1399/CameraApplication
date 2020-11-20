package com.yml.cameraapplication

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_capture_image.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class CaptureImageActivity : AppCompatActivity() {
    private var photoURI: Uri? = null
    private lateinit var imageDir: File
    private val filePath = mutableListOf<String>()
    private lateinit var imageFile: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture_image)

        btn_capture.setOnClickListener {
            openCamera()
        }
        btn_display.setOnClickListener {
            display()
        }
    }

    private fun openCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    photoURI = FileProvider.getUriForFile(
                        this,
                        "com.example.cameraapplication.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, 44)
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 44 && resultCode == RESULT_OK) {
            //val bitmap = data?.extras?.get("data")
            //image.setImageBitmap(bitmap as Bitmap?)
            val bitmap: Bitmap = BitmapFactory.decodeFile(currentPhotoPath)
            image_view.setImageBitmap(bitmap)
            image_view.setImageURI(photoURI)
        }
    }


    private lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }

    }

    private fun display() {
        try {
            imageDir = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.path)

            for (i in imageDir.list().indices) {
                imageFile = File(imageDir.path + "/" + imageDir.list()[i])
                val p = imageFile.path
                filePath.add(p)
            }
            image_recycler_view.layoutManager = LinearLayoutManager(this)
            image_recycler_view.adapter = ImageAdapter(filePath)

        } catch (exception: Exception) {
            Toast.makeText(applicationContext, exception.toString(), Toast.LENGTH_LONG).show()
        }
    }
}