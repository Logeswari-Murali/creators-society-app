package com.example.creatorsociety

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val settingsIcon = findViewById<ImageView>(R.id.settings)

        // Set default fragment
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.sell -> {
                    replaceFragment(CreatorToolkitFragment())
                    true
                }
                R.id.profile -> {
                    replaceFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }

        // Show popup menu when settings icon is clicked
        settingsIcon.setOnClickListener { showPopupMenu(it) }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun showPopupMenu(view: android.view.View) {
        val wrapper = ContextThemeWrapper(this, R.style.PopupMenuStyle) // Apply custom theme
        val popupMenu = PopupMenu(wrapper, view, Gravity.END)
        popupMenu.menu.add("Log out") // Directly adding "Log out" option

        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            if (menuItem.title == "Log out") {
                logoutUser()
                true
            } else {
                false
            }
        }

        popupMenu.show()
    }

    private fun logoutUser() {
        FirebaseAuth.getInstance().signOut()
        Toast.makeText(this, "Logged out!", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, AuthenticationActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
