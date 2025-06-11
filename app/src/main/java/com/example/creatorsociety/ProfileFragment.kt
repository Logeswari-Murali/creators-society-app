package com.example.creatorsociety

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.creatorsociety.CreatorSocietyApp
import com.example.creatorsociety.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadUserProfile()
    }

    private fun loadUserProfile() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser == null) return

        val userId = firebaseUser.uid // Get Firebase UID

        lifecycleScope.launch {
            val user = CreatorSocietyApp.database.userDao().getUser(userId)
            user?.let {
                binding.tvArtistName.text = it.username
                binding.tvArtistType.text = it.atype
                binding.tvArtistNote.text = it.bio
                if (it.profileImagePath.isNotEmpty()) {
                    binding.profileImage.setImageURI(Uri.parse(it.profileImagePath))
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
