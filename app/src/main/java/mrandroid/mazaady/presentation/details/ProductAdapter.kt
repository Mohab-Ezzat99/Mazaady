package mrandroid.mazaady.presentation.details

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import mrandroid.mazaady.databinding.ItemProductBinding
import javax.inject.Inject

class ProductAdapter @Inject constructor() : ListAdapter<String, ProductAdapter.ProductViewHolder>(ITEM_COMPARATOR) {
    private lateinit var listener: OnItemClickListener
    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
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

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    listener.onImgClick(bindingAdapterPosition)
                    setCurrentPosition(bindingAdapterPosition)
                }
            }
        }

        fun bind(model: String) {

        }

    }

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onImgClick(position: Int)
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