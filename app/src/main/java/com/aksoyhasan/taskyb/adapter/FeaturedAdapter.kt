package com.aksoyhasan.taskyb.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aksoyhasan.taskyb.R
import com.aksoyhasan.taskyb.model.Featured
import com.aksoyhasan.taskyb.model.Timeline
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_horizontal_layout.view.*
import kotlinx.android.synthetic.main.item_horizontal_layout.view.item_hr_layout_bg_image
import kotlinx.android.synthetic.main.item_vertical_layout.view.*

class FeaturedAdapter : RecyclerView.Adapter<FeaturedAdapter.FeaturedViewHolder>() {

    inner class FeaturedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Featured>() {
        override fun areItemsTheSame(oldItem: Featured, newItem: Featured): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Featured, newItem: Featured): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturedAdapter.FeaturedViewHolder {
        return FeaturedViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.item_vertical_layout,
                        parent,
                        false
                )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FeaturedAdapter.FeaturedViewHolder, position: Int) {
        val featured = differ.currentList[position]
        holder.itemView.apply {
            val imageUrl = featured.imageUrl
            Glide.with(context).load(imageUrl).into(item_vr_layout_bg_image)
            item_vr_layout_tv_city.text = featured.city
            item_vr_layout_tv_country.text = featured.country
        }
    }
}