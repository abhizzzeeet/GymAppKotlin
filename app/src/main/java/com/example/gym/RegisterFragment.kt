package com.example.gym

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment


/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var fullNameEditText: EditText
    private lateinit var mobileNoEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var registerButton: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        // Initialize views
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

        // Set click listener for the register button
        registerButton.setOnClickListener {
            // Retrieve entered fields
            val fullName = fullNameEditText.text.toString()
            val mobileNo = mobileNoEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            if(password != confirmPassword)
                Toast.makeText(requireContext(), "Password and confirm Password don't match", Toast.LENGTH_SHORT).show()

            // Check if input is valid (for demonstration purposes)
            if (isValidRegistration(fullName, mobileNo, email, password, confirmPassword)) {
                // Successful registration, show a success message
                val message = "Registration successful!\nName: $fullName\nMobile No.: $mobileNo\nEmail: $email"
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            } else {
                // Invalid registration, show an error message
                Toast.makeText(requireContext(), "Invalid registration input", Toast.LENGTH_SHORT).show()
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
        // Replace this with your actual registration logic
        // For demonstration purposes, just check if all fields are non-empty
        return fullName.isNotEmpty() && mobileNo.isNotEmpty() && email.isNotEmpty()
                && password.isNotEmpty() && confirmPassword.isNotEmpty()
    }
}