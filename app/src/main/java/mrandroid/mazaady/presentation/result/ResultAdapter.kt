package mrandroid.mazaady.presentation.result

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import mrandroid.mazaady.databinding.ItemResultBinding
import mrandroid.mazaady.domain.model.ResultModel
import javax.inject.Inject

class ResultAdapter @Inject constructor() :
    ListAdapter<ResultModel, ResultAdapter.ProductViewHolder>(ITEM_COMPARATOR) {
    private lateinit var listener: OnItemClickListener
    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ItemResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    inner class ProductViewHolder(private val binding: ItemResultBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    listener.onImgClick(bindingAdapterPosition)
                    setCurrentPosition(bindingAdapterPosition)
                }
            }
        }

        fun bind(model: ResultModel) {
            binding.apply {
                tvTitle.text = model.title
                tvValue.text = model.value
            }
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
        private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<ResultModel>() {
            override fun areItemsTheSame(
                oldItem: ResultModel,
                newItem: ResultModel
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: ResultModel,
                newItem: ResultModel
            ) = oldItem == newItem
        }
    }
}