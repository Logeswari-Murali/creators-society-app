package com.example.creatorsociety

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CommunityAdapter(
    private var communityList: List<CommunityModel>,
    private val onItemClick: (CommunityModel) -> Unit // Click listener for entire item
) : RecyclerView.Adapter<CommunityAdapter.CommunityViewHolder>() {

    inner class CommunityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgCommunity: ImageView = itemView.findViewById(R.id.imgCommunity)
        val tvCommunityName: TextView = itemView.findViewById(R.id.tvCommunityName)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(communityList[position]) // Handle full item click
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_community, parent, false)
        return CommunityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommunityViewHolder, position: Int) {
        val community = communityList[position]
        holder.tvCommunityName.text = community.name
        holder.imgCommunity.setImageResource(community.imageRes)
    }

    override fun getItemCount(): Int = communityList.size

    fun updateList(newList: List<CommunityModel>) {
        communityList = newList
        notifyDataSetChanged()
    }
}
