package com.example.gym

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class AddGymActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_gym)

        val addMemberButton: Button = findViewById(R.id.addMemberButton)
        addMemberButton.setOnClickListener {
            // Handle button click, for example:
            val organizationName =
                findViewById<EditText>(R.id.organizationNameEditText).text.toString()
            val displayName = findViewById<EditText>(R.id.displayNameEditText).text.toString()
            val email = findViewById<EditText>(R.id.emailEditText).text.toString()
            val phone = findViewById<EditText>(R.id.phoneEditText).text.toString()

            // Create an Intent to navigate to the new activity
            val intent = Intent(this, ProfileActivity::class.java).apply {
                // Pass data as extras
                putExtra("ORGANIZATION_NAME", organizationName)
                putExtra("DISPLAY_NAME", displayName)
                putExtra("EMAIL", email)
                putExtra("PHONE", phone)
            }

            // Start the new activity
            startActivity(intent)
        }
    }
}