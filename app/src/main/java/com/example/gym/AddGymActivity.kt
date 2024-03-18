package com.example.gym

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.firestore.FirebaseFirestore

class AddGymActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_gym)

        // Retrieve userId from intent extras
        val userId = intent.getStringExtra("USER_ID")


        val addMemberButton: Button = findViewById(R.id.addMemberButton)
        val loginGymButton: Button = findViewById(R.id.loginGymButton)


        addMemberButton.setOnClickListener {
            // Retrieve user information from EditText fields
            val organizationName =
                findViewById<EditText>(R.id.organizationNameEditText).text.toString()
            val displayName = findViewById<EditText>(R.id.displayNameEditText).text.toString()
            val email = findViewById<EditText>(R.id.emailEditText).text.toString()
            val phone = findViewById<EditText>(R.id.phoneEditText).text.toString()

//            // Create a new user document in Firestore
//            createUserInFirestore(email, organizationName, displayName, phone)

            // Generate a unique gym ID
            val gymId = db.collection("gyms").document().id

            // Create a new document in "gyms" collection
            db.collection("gyms").document(gymId)
                .set(
                    hashMapOf(
                        "gymId" to gymId,
                        "organizationName" to organizationName,
                        "displayName" to displayName,
                        "email" to email,
                        "phone" to phone,
                        "userId" to userId
                    )
                )
                .addOnSuccessListener {
                    // Document added successfully
                    Toast.makeText(this, "Gym added successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    // Handle any errors
                    Toast.makeText(this, "Failed to add gym: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }


            // Create an Intent to navigate to the new activity
            val intent = Intent(this, ProfileActivity::class.java).apply {
                // Pass data as extras
                putExtra("DISPLAY_NAME", displayName)
                putExtra("Gym Id", gymId)
                putExtra("USER_ID", userId)
            }

            // Start the new activity
            startActivity(intent)
        }
        loginGymButton.setOnClickListener {
            val gymName = findViewById<EditText>(R.id.gymNameEditText).text.toString()
            var loginGymId: String? = null
            db.collection("gyms")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val displayName = document.getString("displayName")
                        if (displayName == gymName) {
                            loginGymId = document.id

                            val intent = Intent(this, ProfileActivity::class.java).apply {
                                // Pass data as extras
                                putExtra("DISPLAY_NAME", displayName)
                                putExtra("Gym Id", loginGymId)
                                putExtra("USER_ID", userId)
                            }
                            startActivity(intent)
                            return@addOnSuccessListener // Return after successful login }
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to login gym: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }
    private fun createUserInFirestore(email: String, organizationName: String, displayName: String, phone: String) {
        // Create a new collection named "users" in Firestore and add a new document with user information
        db.collection("users").document(email)
            .set(
                hashMapOf(
                    "organizationName" to organizationName,
                    "displayName" to displayName,
                    "email" to email,
                    "phone" to phone
                )
            )
            .addOnSuccessListener {
                // Document added successfully
                Toast.makeText(this, "user added successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                // Handle any errors
                Toast.makeText(this, "user not added", Toast.LENGTH_SHORT).show()
            }
        
    }
}