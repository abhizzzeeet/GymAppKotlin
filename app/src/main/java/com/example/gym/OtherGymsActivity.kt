package com.example.gym

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore

class OtherGymsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        // Access Firestore instance
        val db = FirebaseFirestore.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_gyms)

        val userId = intent.getStringExtra("USER_ID")
        val textView = TextView(this)
        textView.text = userId

//        setContentView(textView)
        val container = findViewById<LinearLayout>(R.id.other_gyms_container)

        if (userId != null) {

            // Query gyms collection where ownerId is equal to userId
            db.collection("gyms")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        // Access gym name from each document and print it
                        val gymName = document.getString("displayName")
                        if (gymName != null) {
                            val button = Button(this)
                            button.text = gymName
                            container.addView(button)
                            button.setOnClickListener {
                                // Handle button click
                                val clickedGymName = (it as Button).text.toString()
                                findGymIdAndNavigateToProfile(userId, clickedGymName)
                            }
                            Log.d("OtherGymsActivity", "Gym Name: $gymName")
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("OtherGymsActivity", "Error getting gyms: ", exception)
                }

        }

    }
    private fun findGymIdAndNavigateToProfile(userId: String, gymName: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("gyms")
            .whereEqualTo("userId", userId)
            .whereEqualTo("displayName", gymName)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val gymId = document.id
                    val intent = Intent(this, ProfileActivity::class.java)
                    intent.putExtra("USER_ID", userId)
                    intent.putExtra("GYM_ID", gymId)
                    intent.putExtra("DISPLAY_NAME", gymName)
                    startActivity(intent)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("OtherGymsActivity", "Error finding gym ID: ", exception)
            }
    }
}