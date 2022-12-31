package mrandroid.mazaady.presentation.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import mrandroid.mazaady.databinding.FragmentDetailsBinding
import mrandroid.mazaady.presentation.BindingFragment
import mrandroid.mazaady.util.Dummy
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

        binding.apply {
            bidderAdapter.submitList(Dummy.dummyBiddersList())
            rvBidders.adapter = bidderAdapter

            productAdapter.submitList(Dummy.dummyList())
            rvProducts.adapter = productAdapter
        }
    }
}