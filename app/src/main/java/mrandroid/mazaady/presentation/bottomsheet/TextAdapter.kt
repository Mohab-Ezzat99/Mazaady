package mrandroid.mazaady.presentation.bottomsheet

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import mrandroid.mazaady.databinding.ItemTextBinding
import javax.inject.Inject

class TextAdapter @Inject constructor() :
    ListAdapter<String, TextAdapter.BidderViewHolder>(ITEM_COMPARATOR) {
    private lateinit var listener: OnItemClickListener
    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BidderViewHolder(
        ItemTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    fun setCurrentPosition(position: Int) {
        Log.d("Mohab", "album position $position")
        if (selectedPosition != position && position != -1) {
            selectedPosition = position
            notifyDataSetChanged()
        }
    }

    fun getCurrentPosition() = selectedPosition

    inner class BidderViewHolder(private val binding: ItemTextBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    listener.onTextClick(currentList[bindingAdapterPosition])
                    setCurrentPosition(bindingAdapterPosition)
                }
            }
        }

        fun bind(model: String) {
            binding.apply {
                tvOption.text = model
                if (currentList.size == bindingAdapterPosition - 1)
                    viewLine.isVisible = false
            }
        }

    }

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onTextClick(value: String)
    }

    //check difference
    companion object {
        private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(
                oldItem: String,
                newItem: String
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: String,
                newItem: String
            ) = oldItem == newItem
        }
    }
}