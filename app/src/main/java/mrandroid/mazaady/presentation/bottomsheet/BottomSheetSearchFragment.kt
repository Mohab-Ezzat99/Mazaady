package mrandroid.mazaady.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import mrandroid.mazaady.R
import mrandroid.mazaady.databinding.FragmentBottomSheetSearchBinding

@AndroidEntryPoint
class BottomSheetSearchFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomSheetSearchBinding
    private val args by navArgs<BottomSheetSearchFragmentArgs>()
    private val optionsList get() = args.optionsList.toMutableList()
    private lateinit var textAdapter: TextAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBottomSheetSearchBinding.bind(view)

        binding.apply {
            optionsList.add(0, "Other")
        }

    }
}