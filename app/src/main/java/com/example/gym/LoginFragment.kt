package com.example.gym

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController


import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()

        val editTextEmail = view.findViewById<EditText>(R.id.editTextEmail)
        val editTextMobile = view.findViewById<EditText>(R.id.editTextMobile)
        val editTextPassword = view.findViewById<EditText>(R.id.editTextPassword)
        val loginButton = view.findViewById<Button>(R.id.btnLogin)

        loginButton.setOnClickListener {
            editTextEmail.error = null
            editTextMobile.error = null
            editTextPassword.error = null

            val email = editTextEmail.text.toString()
            val mobile = editTextMobile.text.toString()
            val password = editTextPassword.text.toString()

            if (!isValidEmail(email)) {
                editTextEmail.error = "Invalid email address"
                return@setOnClickListener
            }

            if (!isValidMobileNumber(mobile)) {
                editTextMobile.error = "Invalid mobile number"
                return@setOnClickListener
            }

            if (!isValidPassword(password)) {
                editTextPassword.error = "Password must be at least 6 characters"
                return@setOnClickListener
            }

            // Use Firebase Authentication to sign in the user
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // User login was successful
                        val user = mAuth.currentUser
                        val message = "Login successful!\nEmail: $email"
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

                        // Add your logic to navigate to the next screen or perform other actions
//                        val intent = Intent(requireContext(),AddGymActivity2::class.java)
//                        startActivity(intent)
                        val intent = Intent (activity, AddGymActivity::class.java)
                        startActivity(intent)

                    } else {
                        // User login failed
                        Toast.makeText(requireContext(), "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }

                // Navigate to the second fragment using the NavController







        }
//        val button = view.findViewById<Button>(R.id.button)
//        button.setOnClickListener {
//            // Use Navigation Component to navigate to the SecondFragment
//            findNavController().navigate(R.id.action_loginFragment_to_addGymFragment)
//        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidMobileNumber(mobile: String): Boolean {
        return android.util.Patterns.PHONE.matcher(mobile).matches() && mobile.length == 10
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }
}
