package com.example.creatorsociety

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch

class ProfileSetupActivity : AppCompatActivity() {

    private lateinit var profileImage: CircleImageView
    private lateinit var etArtistName: TextInputEditText
    private lateinit var etArtistType: TextInputEditText
    private lateinit var etArtistNote: TextInputEditText
    private lateinit var btnContinue: Button
    private lateinit var backArrow: ImageView

    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_setup)

        profileImage = findViewById(R.id.profileImage)
        etArtistName = findViewById(R.id.inputArtistName)
        etArtistType = findViewById(R.id.inputArtistType)
        etArtistNote = findViewById(R.id.inputArtistNote)
        btnContinue = findViewById(R.id.btnContinue)
        backArrow = findViewById(R.id.backArrow)

        // Load existing profile if available
        loadUserProfile()

        // Open photo picker when clicking profile image
        profileImage.setOnClickListener { openPhotoPicker() }

        // Save data when clicking "Step In"
        btnContinue.setOnClickListener { saveProfile() }

        // Back button navigation
        backArrow.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri
            contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            profileImage.setImageURI(uri)
        }
    }

    private fun openPhotoPicker() {
        pickImageLauncher.launch(arrayOf("image/*"))
    }

    private fun saveProfile() {
        val username = etArtistName.text.toString().trim()
        val atype = etArtistType.text.toString().trim()
        val bio = etArtistNote.text.toString().trim()

        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = firebaseUser.uid // Get Firebase UID

        if (username.isEmpty() || atype.isEmpty()) {
            etArtistName.error = "Required"
            etArtistType.error = "Required"
            return
        }

        val user = UserEntity(
            userId = userId,  // Store Firebase UID as userId
            username = username,
            atype = atype,
            bio = bio,
            profileImagePath = imageUri?.toString() ?: ""
        )

        lifecycleScope.launch {
            CreatorSocietyApp.database.userDao().insertUser(user)
            startActivity(Intent(this@ProfileSetupActivity, ChooseCommunityActivity::class.java))
            finish()
        }
    }



    private fun loadUserProfile() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser == null) return

        val userId = firebaseUser.uid // Get Firebase UID

        lifecycleScope.launch {
            val user = CreatorSocietyApp.database.userDao().getUser(userId)
            user?.let {
                etArtistName.setText(it.username)
                etArtistType.setText(it.atype)
                etArtistNote.setText(it.bio)
                if (it.profileImagePath.isNotEmpty()) {
                    profileImage.setImageURI(Uri.parse(it.profileImagePath))
                }
            }
        }
    }
}
