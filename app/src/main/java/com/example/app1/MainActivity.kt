package com.example.app1

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity(), View.OnClickListener {
    // Create variables to hold user data
    private var mFirstName: String? = null
    private var mMiddleName: String? = null
    private var mLastName: String? = null
    private var mUserPic: ImageView? = null

    // Create variables for UI elements we need to control
    private var mEtFirstName: EditText? = null
    private var mEtMiddleName: EditText? = null
    private var mEtLastName: EditText? = null
    private var mButtonSubmit: Button? = null
    private var mButtonCamera: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Retrieve UI elements
        mEtFirstName = findViewById(R.id.et_firstName)
        mEtMiddleName = findViewById(R.id.et_middleName)
        mEtLastName = findViewById(R.id.et_lastName)
        mButtonSubmit = findViewById(R.id.button_submit)
        mButtonCamera = findViewById(R.id.button_pic)

        // Setup listeners
        if (mButtonSubmit != null)
            mButtonSubmit!!.setOnClickListener(this)

        if (mButtonCamera != null)
            mButtonCamera!!.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.button_submit -> {

                // Check if user has entered their name (Middle name is optional)
                if (mEtFirstName!!.text.isNullOrBlank() || mEtLastName!!.text.isNullOrBlank()) {
                    Toast.makeText(this@MainActivity, "Please enter your full name.", Toast.LENGTH_SHORT)
                        .show()
                    return
                }

                // Record name
                mFirstName = mEtFirstName!!.text.toString()
                mMiddleName = mEtMiddleName!!.text.toString()
                mLastName = mEtLastName!!.text.toString()
            }
            R.id.button_pic -> {

                // Open camera
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                try {
                    cameraActivity.launch(cameraIntent)
                }
                catch (ex: ActivityNotFoundException) {
                    Toast.makeText(this@MainActivity, "Something went wrong.\nPlease try again.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private val cameraActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        // Check if the result is okay
        if (result.resultCode != RESULT_OK)
            return@registerForActivityResult

        val imageBitmap = data.extras.get("data") as Bitmap
        imageView.setImageBitmap(imageBitmap)

        mUserPic!!.setImageBitmap(thumbnailImage)
        }
    }
}