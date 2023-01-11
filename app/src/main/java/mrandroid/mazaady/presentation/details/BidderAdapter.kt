package mrandroid.mazaady.presentation.details

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import mrandroid.mazaady.databinding.ItemBidderBinding
import javax.inject.Inject

class BidderAdapter @Inject constructor() : ListAdapter<Int, BidderAdapter.BidderViewHolder>(ITEM_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BidderViewHolder(
        ItemBidderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: BidderViewHolder, position: Int) {
        getItem(position).let { holder.bind(it) }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class BidderViewHolder(private val binding: ItemBidderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: Int) {
            binding.apply {
                ivProfile.setImageResource(model)
            }
        }

    }

    //check difference
    companion object {
        private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<Int>() {
            override fun areItemsTheSame(
                oldItem: Int,
                newItem: Int
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: Int,
                newItem: Int
            ) = oldItem == newItem
        }
    }
}