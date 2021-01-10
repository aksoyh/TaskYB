package com.aksoyhasan.taskyb.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aksoyhasan.taskyb.R
import com.aksoyhasan.taskyb.model.Mention
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_horizontal_layout.view.*
import kotlinx.android.synthetic.main.item_horizontal_layout.view.item_hr_layout_bg_image
import kotlinx.android.synthetic.main.item_mentions_recyclerview_list.view.*

class MentionsAdapter : RecyclerView.Adapter<MentionsAdapter.MentionViewHolder>() {

    inner class MentionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Mention>() {
        override fun areItemsTheSame(oldItem: Mention, newItem: Mention): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Mention, newItem: Mention): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MentionsAdapter.MentionViewHolder {
        return MentionViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.item_mentions_recyclerview_list,
                        parent,
                        false
                )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MentionsAdapter.MentionViewHolder, position: Int) {
        val mention = differ.currentList[position]
        holder.itemView.apply {
            val imageUrl = mention.profileImage
            Glide.with(context).load(imageUrl).into(item_mentions_rv_iv)
            item_hr_layout_tv_mentions_name.text = mention.fullname
            item_hr_layout_tv_mentions_account.text = mention.userName
            if (mention.isFollowing) {
                item_mentions_rv_btn_follow.visibility = View.VISIBLE
            } else {
                item_mentions_rv_btn_follow.visibility = View.INVISIBLE
            }
        }
    }
}