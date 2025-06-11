package com.example.creatorsociety

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class CategoryAdapter(
    private val categories: List<CategoryModel>,
    private val onCategoryClick: (CategoryModel) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgCategory: ImageView = view.findViewById(R.id.imgCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]

        // Set the correct image dynamically
        val categoryImages = listOf(
            R.drawable.des_aes,
            R.drawable.nat_fan,
            R.drawable.hand_diy,
            R.drawable.culi_arts,
            R.drawable.per_arts,
            R.drawable.photo_film,
            R.drawable.visual_arts,
            R.drawable.lit_w
        )

        holder.imgCategory.setImageResource(categoryImages[position % categoryImages.size])

        // Highlight selected
        holder.imgCategory.alpha = if (category.isSelected) 0.5f else 1.0f

        // Click logic
        holder.itemView.setOnClickListener {
            category.isSelected = !category.isSelected
            notifyItemChanged(position)
            onCategoryClick(category)
        }
    }

    override fun getItemCount() = categories.size
}
