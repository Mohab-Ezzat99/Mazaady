package mrandroid.mazaady.presentation.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import mrandroid.mazaady.R
import mrandroid.mazaady.databinding.FragmentCategoriesBinding
import mrandroid.mazaady.presentation.BindingFragment

@AndroidEntryPoint
class CategoriesFragment : BindingFragment<FragmentCategoriesBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentCategoriesBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnDetails.setOnClickListener {
                findNavController().navigate(R.id.detailsFragment)
            }
        }

    }
}