package com.example.app1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class LoginActivity : AppCompatActivity() {
    // Key constants
    companion object {
        private const val FIRST_NAME_KEY = "First Name"
        private const val LAST_NAME_KEY = "Last Name"
    }

    // Variables for user's name and UI text view
    private var mFullName: String? = null
    private var mTvLogin: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Get intent and text view
        val receivedIntent = intent
        mTvLogin = findViewById(R.id.tv_login)

        // Get the name submitted and show that the user logged in
        mFullName = receivedIntent.getStringExtra(FIRST_NAME_KEY).plus(" ")
            .plus(receivedIntent.getStringExtra(LAST_NAME_KEY))

        if (mTvLogin != null && !mFullName.isNullOrBlank())
            mTvLogin!!.text = mFullName.plus(getString(R.string.loginMessage))
    }
}