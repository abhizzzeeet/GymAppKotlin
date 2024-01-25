package com.example.gym

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast



/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)


    }

    

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Accessing the views from the layout
        val editTextEmail = view.findViewById<EditText>(R.id.editTextEmail)
        val editTextMobile = view.findViewById<EditText>(R.id.editTextMobile)
        val editTextPassword = view.findViewById<EditText>(R.id.editTextPassword)
        val loginButton = view.findViewById<Button>(R.id.btnLogin)

        // Set click listener for the login button
        loginButton.setOnClickListener {
            val email = editTextEmail.text.toString()
            val mobile = editTextMobile.text.toString()
            val password = editTextPassword.text.toString()

            // Validate email
            if (!isValidEmail(email)) {
                editTextEmail.error = "Invalid email address"
                return@setOnClickListener
            }

            // Validate mobile number
            if (!isValidMobileNumber(mobile)) {
                editTextMobile.error = "Invalid mobile number"
                return@setOnClickListener
            }

            // Validate password
            if (!isValidPassword(password)) {
                editTextPassword.error = "Password must be at least 6 characters"
                return@setOnClickListener
            }

            // If all fields are valid, perform login logic here
            // For demonstration purposes, you can print the values
            Toast.makeText(requireContext(), "Login Successfull", Toast.LENGTH_SHORT).show()
        }
    }
    private fun isValidEmail(email: String): Boolean {
        // Basic email validation
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidMobileNumber(mobile: String): Boolean {
        // Basic mobile number validation (assumes 10-digit number)
        return android.util.Patterns.PHONE.matcher(mobile).matches() && mobile.length == 10
    }

    private fun isValidPassword(password: String): Boolean {
        // Basic password validation (at least 6 characters)
        return password.length >= 6
    }
}