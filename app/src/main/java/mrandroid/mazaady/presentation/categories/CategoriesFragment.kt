package mrandroid.mazaady.presentation.categories

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import mrandroid.mazaady.R
import mrandroid.mazaady.data.remote.dto.CategoriesData
import mrandroid.mazaady.databinding.FragmentCategoriesBinding
import mrandroid.mazaady.presentation.BindingFragment
import mrandroid.mazaady.presentation.dialog.LoadingDialog
import mrandroid.mazaady.util.showToast
import mrandroid.mazaady.util.spinnerAdapter
import mrandroid.mazaady.util.state.UiState

@AndroidEntryPoint
class CategoriesFragment : BindingFragment<FragmentCategoriesBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentCategoriesBinding::inflate

    private val viewModel: CategoriesViewModel by viewModels()
    private var categoriesData: CategoriesData? = null
    private var mainCatsList: List<String> = emptyList()
    private var subCatsList: List<String> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAllCats()

        binding.apply {
            inMainCategories.itl.hint = "Main Categories"
            inSubCategories.itl.hint = "Sub Categories"
            inProcessType.itl.hint = "Process Type"
            inSpecifyHere.itl.hint = "Specify Here"
            inBrand.itl.hint = "Brand"
            inModel.itl.hint = "Model"
            inType.itl.hint = "Type"
            inTransmissionType.itl.hint = "Transmission Type"
            inFuelType.itl.hint = "Fuel Type"
            inCondition.itl.hint = "Condition"

            inMainCategories.at.inputType = InputType.TYPE_NULL
            inSubCategories.at.inputType = InputType.TYPE_NULL
            inProcessType.at.inputType = InputType.TYPE_NULL
            inSpecifyHere.at.inputType = InputType.TYPE_NULL

        }

        fetchCategoriesState()
    }

    private fun fetchCategoriesState() {
        lifecycleScope.launchWhenStarted {
            viewModel.categoriesState.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        LoadingDialog.showDialog()
                    }
                    is UiState.Error -> {
                        LoadingDialog.dismissDialog()
                        showToast(state.message!!.asString(requireContext()))
                    }
                    is UiState.Success -> {
                        LoadingDialog.dismissDialog()
                        categoriesData = state.data?.data
                        setupMainCategories()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setupMainCategories() {
        binding.apply {
            categoriesData?.let { data ->
                mainCatsList = data.categories.map { it.name ?: "Empty text" }
                inMainCategories.at.setAdapter(spinnerAdapter(requireContext(), mainCatsList))
                inMainCategories.at.setOnItemClickListener { adapterView, view, index, l ->
                    subCatsList = data.categories[index].children.map { it.name!! }
                    inSubCategories.at.setAdapter(spinnerAdapter(requireContext(), subCatsList))
                    inSubCategories.at.setText("")
                }
            }
        }
    }
}