package mrandroid.mazaady.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import mrandroid.mazaady.R
import mrandroid.mazaady.databinding.FragmentSearchBinding
import mrandroid.mazaady.presentation.SharedViewModel
import mrandroid.mazaady.presentation.categories.CategoriesViewModel
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : BottomSheetDialogFragment(), TextAdapter.OnItemClickListener {

    private lateinit var binding: FragmentSearchBinding
    private val args by navArgs<SearchFragmentArgs>()
    private val optionsList get() = args.optionsList
    private val title get() = args.title

    private val viewModelShared: SharedViewModel by activityViewModels()

    @Inject
    lateinit var textAdapter: TextAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)

        textAdapter.setListener(this)
        binding.apply {
            tvTitle.text = title

            val displayOptions = ArrayList<String>()
            displayOptions.add("other")
            displayOptions.addAll(optionsList)
            textAdapter.submitList(displayOptions)
            rvOptions.adapter = textAdapter

            etSearch.addTextChangedListener { text ->
                if (text.toString().isEmpty()) textAdapter.submitList(displayOptions)
                else {
                    val list = displayOptions.filter {
                        it.lowercase().contains(text.toString().lowercase())
                    }
                    textAdapter.submitList(null)
                    textAdapter.submitList(list)
                }
            }
        }

    }

    override fun onTextClick(value: String) {
        viewModelShared.selectionValue.value = value
        findNavController().popBackStack()
    }
}