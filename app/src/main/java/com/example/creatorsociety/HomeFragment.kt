package com.example.creatorsociety

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var communityAdapter: CommunityAdapter
    private lateinit var searchBar: EditText
    private val communityList = mutableListOf<CommunityModel>()
    private val filteredList = mutableListOf<CommunityModel>() // For searching

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewCommunities)
        searchBar = view.findViewById(R.id.searchBar)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Load selected communities from SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences(
            "CreatorSocietyPrefs",
            AppCompatActivity.MODE_PRIVATE
        )
        val selectedCommunities =
            sharedPreferences.getStringSet("selectedCommunities", emptySet()) ?: emptySet()

        val allCommunities = mapOf(
            "Design & Aesthetics" to R.drawable.des_aes,
            "Nature & Fantasy" to R.drawable.nat_fan,
            "Handicrafts & DIY" to R.drawable.hand_diy,
            "Culinary Arts" to R.drawable.culi_arts,
            "Performing Arts" to R.drawable.per_arts,
            "Photography & Filmmaking" to R.drawable.photo_film,
            "Visual Arts" to R.drawable.visual_arts,
            "Writing & Literature" to R.drawable.lit_w
        )

        selectedCommunities.forEach { name ->
            communityList.add(
                CommunityModel(
                    name,
                    "Active now",
                    allCommunities[name] ?: R.drawable.visual_arts
                )
            )
        }

        filteredList.addAll(communityList) // Initially show all communities

        // Initialize adapter with click listener
        communityAdapter = CommunityAdapter(filteredList) { community ->
            openChatActivity(community)
        }

        recyclerView.adapter = communityAdapter

        // Add search functionality
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterCommunities(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        return view
    }

    // Function to filter communities based on search input
    private fun filterCommunities(query: String) {
        filteredList.clear()
        if (query.isEmpty()) {
            filteredList.addAll(communityList)
        } else {
            val searchText = query.lowercase()
            for (community in communityList) {
                if (community.name.lowercase().contains(searchText)) {
                    filteredList.add(community)
                }
            }
        }
        communityAdapter.notifyDataSetChanged()
    }

    // Function to show dropdown list of communities
    private fun showCommunityDropdown(view: View) {
        val popupMenu = PopupMenu(ContextThemeWrapper(requireContext(), R.style.CustomPopupMenu), view)

        filteredList.forEachIndexed { index, community ->
            popupMenu.menu.add(Menu.NONE, index, index, community.name)
        }

        popupMenu.setOnMenuItemClickListener { menuItem ->
            val selectedCommunity = filteredList[menuItem.itemId]
            openChatActivity(selectedCommunity)
            true
        }

        // Manually set the background

        popupMenu.show()
    }


    // Function to open ChatActivity
    private fun openChatActivity(community: CommunityModel) {
        Log.d("HomeFragment", "Opening ChatActivity for: ${community.name}") // Debugging log

        val intent = Intent(requireContext(), ChatActivity::class.java)
        intent.putExtra("communityName", community.name)
        startActivity(intent)
    }
}
