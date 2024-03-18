package com.example.gym

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.gym.databinding.ActivityProfileBinding


class ProfileActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityProfileBinding
    private var displayName: String? = null // Variable to store Display_Name
    private var gymId: String? = null // Variable to store Gym Id
    private lateinit var sharedViewModel: SharedViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel
        sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        sharedViewModel.gymId = intent.getStringExtra("Gym Id")

        val userId = intent.getStringExtra("USER_ID")
        displayName = intent.getStringExtra("DISPLAY_NAME")
        gymId = intent.getStringExtra("Gym Id")

        setSupportActionBar(binding.appBarProfile.toolbar)

        binding.appBarProfile.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_profile)

        // Dynamically add item in menu for changing Gyms
        // Get reference to NavigationView
        val navigationView: NavigationView = findViewById(R.id.nav_view)

        // Get reference to the menu
        val menu: Menu = navigationView.menu

        // Inflate a menu item
        val newMenuItem: MenuItem = menu.add(Menu.NONE, Menu.NONE, 1, displayName)

        // Set an icon if needed
        // newMenuItem.setIcon(R.drawable.your_icon)

        // Set an onClickListener if needed
        newMenuItem.setOnMenuItemClickListener {
            // Handle click event
            val intent = Intent(this, OtherGymsActivity::class.java).apply {
                // Pass data as extras
                putExtra("USER_ID", userId)
            }
            startActivity(intent)
//            startActivity(Intent(this, OtherGymsActivity::class.java))
            true
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Retrieve Display_Name passed from AddGymActivity


        // Set Display_Name in the header of the navigation drawer
        val headerView = navView.getHeaderView(0)
        val textViewDisplayName: TextView = headerView.findViewById(R.id.textViewDisplayName)
        textViewDisplayName.text = displayName


        }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.profile, menu)
        return true
    }



    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_profile)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}