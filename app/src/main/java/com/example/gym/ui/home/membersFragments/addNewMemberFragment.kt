package com.example.gym.ui.home.membersFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.example.gym.R
import com.example.gym.SharedViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [addNewMemberFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class addNewMemberFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private lateinit var etMemberId: EditText
    private lateinit var etMemberName: EditText
    private lateinit var etMobileNumber: EditText
    private lateinit var btnSubmit: Button

    private lateinit var sharedViewModel: SharedViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        // Access the gymId value
        val gymId: String = sharedViewModel.gymId as String

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_new_member, container, false)

        // Initialize Firebase Firestore and Firebase Auth
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        etMemberId = view.findViewById(R.id.etMemberId)
        etMemberName = view.findViewById(R.id.etMemberName)
        etMobileNumber = view.findViewById(R.id.etMobileNumber)
        btnSubmit = view.findViewById(R.id.btnSubmit)

        // Set click listener for submit button
        btnSubmit.setOnClickListener {
            val memberId = etMemberId.text.toString().trim()
            val memberName = etMemberName.text.toString().trim()
            val mobileNumber = etMobileNumber.text.toString().trim()

            // Get the current user's ID
            val userId = auth.currentUser?.uid

            if (memberId.isEmpty() || memberName.isEmpty() || mobileNumber.isEmpty() || userId.isNullOrEmpty()) {
                // Handle empty fields

            }

            // Add the member to the appropriate gym's subcollection

            val memberData = hashMapOf(
                "name" to memberName,
                "memberId" to memberId,
                "mobileNumber" to mobileNumber,
                "userId" to userId
                // Add any other member-specific fields here
            )

            db.collection("gyms").document(gymId).collection("members").document(memberId)
                .set(memberData)
                .addOnSuccessListener {
                    Log.d(TAG, "Member successfully added!")
                    // Clear the form fields
                    etMemberId.text.clear()
                    etMemberName.text.clear()
                    etMobileNumber.text.clear()
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error adding member", e)
                }
        }


        return view
    }

    companion object {
        private const val TAG = "AddMemberActivity"
    }

}