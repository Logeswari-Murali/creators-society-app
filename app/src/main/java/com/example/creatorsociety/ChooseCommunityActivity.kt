package com.example.creatorsociety

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ChooseCommunityActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnProceed: Button
    private val selectedCategories = mutableListOf<CategoryModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_community)

        recyclerView = findViewById(R.id.recyclerView)
        btnProceed = findViewById(R.id.btnProceed)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2) // 2 columns


        // Sample categories (replace with actual images)
        val categories = listOf(
            CategoryModel("Design & Aesthetics", R.drawable.des_aes),
            CategoryModel("Nature & Fantasy", R.drawable.nat_fan),
            CategoryModel("Handicrafts & DIY", R.drawable.hand_diy),
            CategoryModel("Culinary Arts", R.drawable.culi_arts),
            CategoryModel("Performing Arts", R.drawable.per_arts),
            CategoryModel("Photography & Filmmaking", R.drawable.photo_film),
            CategoryModel("Visual Arts", R.drawable.visual_arts),
            CategoryModel("Writing & Literature", R.drawable.lit_w)
        )

        val adapter = CategoryAdapter(categories) { category ->
            if (category.isSelected) {
                selectedCategories.add(category)
            } else {
                selectedCategories.remove(category)
            }

            // Show "Continue" button if at least one category is selected
            btnProceed.visibility = if (selectedCategories.isNotEmpty()) View.VISIBLE else View.GONE
        }

        // RecyclerView setup
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter

        btnProceed.setOnClickListener {
            val selectedNames = selectedCategories.map { it.name }.toSet()

            // Save in SharedPreferences
            val sharedPreferences = getSharedPreferences("CreatorSocietyPrefs", MODE_PRIVATE)
            sharedPreferences.edit().putStringSet("selectedCommunities", selectedNames).apply()

            Toast.makeText(this, "Saved: $selectedNames", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }


    }
}
