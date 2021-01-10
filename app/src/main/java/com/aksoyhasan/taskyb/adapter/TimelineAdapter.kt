package com.aksoyhasan.taskyb.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aksoyhasan.taskyb.R
import com.aksoyhasan.taskyb.model.Timeline
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_horizontal_layout.view.*

class TimelineAdapter : RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder>() {

    inner class TimelineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class ItemViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {
        private val horizontalRecyclerView: RecyclerView
        val horizontalAdapter: MentionsImageAdapter

        init {
            val context = itemView.context
            horizontalRecyclerView = itemView.findViewById(R.id.item_hr_layout_rv_mentions)
            horizontalRecyclerView?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            horizontalAdapter = MentionsImageAdapter()
            horizontalRecyclerView.adapter = horizontalAdapter
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Timeline>() {
        override fun areItemsTheSame(oldItem: Timeline, newItem: Timeline): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Timeline, newItem: Timeline): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineAdapter.TimelineViewHolder {
        return TimelineViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.item_horizontal_layout,
                        parent,
                        false
                )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TimelineAdapter.TimelineViewHolder, position: Int) {
        val timeline = differ.currentList[position]
        holder.itemView.apply {
            val imageUrl = timeline.imageUrl
            Glide.with(context).load(imageUrl).into(item_hr_layout_bg_image)
            item_hr_layout_tv_title.text = timeline.title
            item_hr_layout_tv_included_suggestions.text = "${timeline.countryCount} COUNTRIES"
            item_hr_layout_tv_published_time.text = timeline.date
            // Adding mentions to list and send it to listener.
        }
    }
}