package com.example.app1

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap

class MainActivity : AppCompatActivity(), View.OnClickListener {

    // Key constants
    companion object {
        private const val FIRST_NAME_KEY = "First Name"
        private const val MIDDLE_NAME_KEY = "Middle Name"
        private const val LAST_NAME_KEY = "Last Name"
        private const val PICTURE_KEY = "Picture"
    }

    // Create variables for UI elements we need to control
    private var mEtFirstName: EditText? = null
    private var mEtMiddleName: EditText? = null
    private var mEtLastName: EditText? = null
    private var mButtonSubmit: Button? = null
    private var mButtonCamera: Button? = null
    private var mIvPic : ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Retrieve UI elements
        mEtFirstName = findViewById(R.id.et_firstName)
        mEtMiddleName = findViewById(R.id.et_middleName)
        mEtLastName = findViewById(R.id.et_lastName)
        mButtonSubmit = findViewById(R.id.button_submit)
        mButtonCamera = findViewById(R.id.button_pic)
        mIvPic = findViewById(R.id.iv_pic)

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

                // Start the login screen
                val loginIntent = Intent(this, LoginActivity::class.java)
                loginIntent.putExtra(FIRST_NAME_KEY, mEtFirstName!!.text.toString())
                loginIntent.putExtra(LAST_NAME_KEY, mEtLastName!!.text.toString())
                this.startActivity(loginIntent)
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // Get names
        val mFirstName = mEtFirstName!!.text.toString()
        val mMiddleName = mEtMiddleName!!.text.toString()
        val mLastName = mEtLastName!!.text.toString()

        // Store in saved state
        outState.putString(FIRST_NAME_KEY, mFirstName)
        outState.putString(MIDDLE_NAME_KEY, mMiddleName)
        outState.putString(LAST_NAME_KEY, mLastName)
        if (mIvPic!!.drawable != null)
            outState.putParcelable(PICTURE_KEY, mIvPic!!.drawable.toBitmap())
    }

    override fun onRestoreInstanceState(inState: Bundle) {
        super.onRestoreInstanceState(inState)

        // Retrieve saved names
        mEtFirstName!!.setText(inState.getString(FIRST_NAME_KEY))
        mEtMiddleName!!.setText(inState.getString(MIDDLE_NAME_KEY))
        mEtLastName!!.setText(inState.getString(LAST_NAME_KEY))

        // Retrieve picture, but only if it exists
        if (!inState.containsKey(PICTURE_KEY))
            return

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= 33)
            mIvPic!!.setImageBitmap(inState.getParcelable(PICTURE_KEY, Bitmap::class.java))
        else
            mIvPic!!.setImageBitmap(inState.getParcelable(PICTURE_KEY))
    }

    private val cameraActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        // Check if the result is okay
        if (result.resultCode != RESULT_OK)
            return@registerForActivityResult

        if (Build.VERSION.SDK_INT >= 33) {
            val thumbnailImage = result.data!!.getParcelableExtra("data", Bitmap::class.java)
            mIvPic!!.setImageBitmap(thumbnailImage)
        }
        else {
            @Suppress("DEPRECATION") val thumbnailImage = result.data!!.getParcelableExtra<Bitmap>("data")
            mIvPic!!.setImageBitmap(thumbnailImage)
        }
    }
}