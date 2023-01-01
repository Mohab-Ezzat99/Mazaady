package mrandroid.mazaady.presentation.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import mrandroid.mazaady.R
import mrandroid.mazaady.databinding.FragmentResultBinding
import mrandroid.mazaady.presentation.BindingFragment
import mrandroid.mazaady.presentation.bottomsheet.SearchFragmentArgs
import javax.inject.Inject

@AndroidEntryPoint
class ResultFragment : BindingFragment<FragmentResultBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentResultBinding::inflate

    private val args by navArgs<ResultFragmentArgs>()
    private val resultList get() = args.resultList

    @Inject
    lateinit var resultAdapter: ResultAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            resultAdapter.submitList(resultList.toList())
            rvResult.adapter = resultAdapter

            btnSecondPage.setOnClickListener {
                findNavController().navigate(R.id.detailsFragment)
            }
        }

    }

}