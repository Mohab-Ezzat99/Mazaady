package mrandroid.mazaady.presentation.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import mrandroid.mazaady.databinding.FragmentDetailsBinding
import mrandroid.mazaady.databinding.ItemSliderBinding
import mrandroid.mazaady.presentation.BindingFragment
import mrandroid.mazaady.util.Dummy
import org.imaginativeworld.whynotimagecarousel.listener.CarouselListener
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import org.imaginativeworld.whynotimagecarousel.utils.setImage
import javax.inject.Inject

@AndroidEntryPoint
class DetailsFragment : BindingFragment<FragmentDetailsBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentDetailsBinding::inflate

    @Inject
    lateinit var bidderAdapter: BidderAdapter

    @Inject
    lateinit var productAdapter: ProductAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSlider()

        binding.apply {
            bidderAdapter.submitList(Dummy.dummyBiddersList())
            rvBidders.adapter = bidderAdapter

            productAdapter.submitList(Dummy.dummyList())
            rvProducts.adapter = productAdapter
        }
    }

    private fun setupSlider() {
        binding.inSlider.apply {
            carouselSlider.registerLifecycle(lifecycle)
            carouselSlider.setData(Dummy.dummySliderList())
            carouselSlider.setIndicator(dotSlider)
            carouselSlider.carouselListener = object : CarouselListener {
                override fun onCreateViewHolder(
                    layoutInflater: LayoutInflater,
                    parent: ViewGroup
                ): ViewBinding {
                    return ItemSliderBinding.inflate(
                        layoutInflater,
                        parent,
                        false
                    )
                }

                override fun onBindViewHolder(
                    binding: ViewBinding,
                    item: CarouselItem,
                    position: Int
                ) {
                    val currentBinding = binding as ItemSliderBinding
                    currentBinding.ivImg.apply {
                        scaleType = ImageView.ScaleType.CENTER_CROP
                        setImage(item)
                    }
                }
            }
        }
    }
}