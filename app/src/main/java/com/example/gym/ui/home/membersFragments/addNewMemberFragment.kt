package com.example.gym.ui.home.membersFragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.lifecycle.ViewModelProvider
import com.example.gym.R
import com.example.gym.SharedViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

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

    private lateinit var trainerSpinner: Spinner // Declare Spinner variable

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

//        etMemberId = view.findViewById(R.id.etMemberId)
        etMemberName = view.findViewById(R.id.etMemberName)
        etMobileNumber = view.findViewById(R.id.etMobileNumber)
        btnSubmit = view.findViewById(R.id.btnSubmit)

        trainerSpinner = view.findViewById(R.id.trainerSpinner) // Initialize Spinner

        // Set click listener for submit button
        btnSubmit.setOnClickListener {
            val memberName = etMemberName.text.toString().trim()
            val mobileNumber = etMobileNumber.text.toString().trim()

            // Get the current user's ID
            val userId = auth.currentUser?.uid


            // Check if the members collection exists inside the gym document, if not, create it
            val gymRef = db.collection("gyms").document(gymId)
            gymRef.collection("members").get()
                .addOnSuccessListener { querySnapshot ->
                    if (querySnapshot.isEmpty) {
                        // Members collection doesn't exist, create it and add the first member
                        val memberData = hashMapOf(
                            "name" to memberName,
                            "memberId" to "1", // memberId starts with 1
                            "mobileNumber" to mobileNumber,
                            "userId" to userId
                            // Add any other member-specific fields here
                        )

                        gymRef.collection("members")
                            .document("1") // Use memberId 1
                            .set(memberData)
                            .addOnSuccessListener {
                                Log.d(TAG, "Member successfully added!")
                                // Clear the form fields
                                etMemberName.text.clear()
                                etMobileNumber.text.clear()
                            }
                            .addOnFailureListener { e ->
                                Log.e(TAG, "Error adding member", e)
                            }
                    } else {
                        // Members collection exists, fetch the last memberId and increment it
                        gymRef.collection("members")
                            .orderBy("memberId", Query.Direction.DESCENDING)
                            .limit(1)
                            .get()
                            .addOnSuccessListener { querySnapshot ->
                                var newMemberId = 1 // Default value for the first member
                                if (!querySnapshot.isEmpty) {
                                    val lastMemberId = querySnapshot.documents[0].get("memberId") as String
                                    newMemberId = lastMemberId.toInt() + 1
                                }

                                // Add the new member with the incremented memberId
                                val memberData = hashMapOf(
                                    "name" to memberName,
                                    "memberId" to newMemberId.toString(),
                                    "mobileNumber" to mobileNumber,
                                    "userId" to userId
                                    // Add any other member-specific fields here
                                )

                                gymRef.collection("members")
                                    .document(newMemberId.toString())
                                    .set(memberData)
                                    .addOnSuccessListener {
                                        Log.d(TAG, "Member successfully added!")
                                        // Clear the form fields
                                        etMemberName.text.clear()
                                        etMobileNumber.text.clear()
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e(TAG, "Error adding member", e)
                                    }
                            }
                            .addOnFailureListener { e ->
                                Log.e(TAG, "Error fetching last memberId", e)
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error checking members collection", e)
                }
        }


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Define options for the drop-down
        val options = arrayOf("Trainer 1", "Trainer 2", "Trainer 3")

        // Create an ArrayAdapter
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        trainerSpinner.adapter = adapter
        // Set item selected listener to capture selected option

         var selectedOption: String? = null
        // Set item selected listener to capture selected option
        trainerSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedOption = options[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case when nothing is selected, if needed
            }
        }

    }

}