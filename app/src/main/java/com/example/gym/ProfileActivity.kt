package com.example.gym

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Retrieve data from intent extras
        val organizationName = intent.getStringExtra("ORGANIZATION_NAME")
        val displayName = intent.getStringExtra("DISPLAY_NAME")
        val email = intent.getStringExtra("EMAIL")
        val phone = intent.getStringExtra("PHONE")

        // Display the profile information
        val organizationNameTextView = findViewById<TextView>(R.id.organizationNameTextView)
        val displayNameTextView = findViewById<TextView>(R.id.displayNameTextView)
        val emailTextView = findViewById<TextView>(R.id.emailTextView)
        val phoneTextView = findViewById<TextView>(R.id.phoneTextView)

        organizationNameTextView.text = "Organization Name: $organizationName"
        displayNameTextView.text = "Display Name: $displayName"
        emailTextView.text = "Email: $email"
        phoneTextView.text = "Phone: $phone"
    }
}