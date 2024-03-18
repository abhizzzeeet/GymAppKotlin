package com.example.gym

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterFragment : Fragment() {
    private lateinit var fullNameEditText: EditText
    private lateinit var mobileNoEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var registerButton: Button
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        fullNameEditText = view.findViewById(R.id.fullNameEditText)
        mobileNoEditText = view.findViewById(R.id.mobileNoEditText)
        emailEditText = view.findViewById(R.id.emailEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)
        confirmPasswordEditText = view.findViewById(R.id.confirmPasswordEditText)
        registerButton = view.findViewById(R.id.registerButton)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerButton.setOnClickListener {
            val fullName = fullNameEditText.text.toString()
            val mobileNo = mobileNoEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            if (isValidRegistration(fullName, mobileNo, email, password, confirmPassword)) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Registration success
                            val user = FirebaseAuth.getInstance().currentUser
                            val message =
                                "Registration successful!\nName: $fullName\nMobile No.: $mobileNo\nEmail: $email"
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

                            // Create a new collection for the user
                            user?.uid?.let { userId ->
                                val userCollection = db.collection("users").document(userId).collection("userData")

                                // You can add more data to store in the document
                                val userData = hashMapOf(
                                    "fullName" to fullName,
                                    "mobileNo" to mobileNo,
                                    "email" to email
                                )

                                userCollection.add(userData)
                                    .addOnSuccessListener {
                                        // Handle success
                                        Toast.makeText(context, "User registered successfully!", Toast.LENGTH_SHORT).show()
                                        // You may navigate to another fragment or perform other actions here
                                    }
                                    .addOnFailureListener { e ->
                                        // Handle failures
                                        Toast.makeText(
                                            requireContext(),
                                            "Failed to create user data: ${e.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }

                            // Navigate to LoginFragment upon successful registration

                        } else {
                            // Registration failed
                            Toast.makeText(
                                requireContext(),
                                "Registration failed: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(requireContext(), "Invalid registration input", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun isValidRegistration(
        fullName: String,
        mobileNo: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (fullName.isEmpty() || mobileNo.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(requireContext(), "All fields must be filled", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password != confirmPassword) {
            Toast.makeText(
                requireContext(),
                "Password and confirm password do not match",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }
}

