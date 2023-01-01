package mrandroid.mazaady.presentation.categories

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import mrandroid.mazaady.R
import mrandroid.mazaady.data.remote.dto.CategoriesData
import mrandroid.mazaady.databinding.FragmentCategoriesBinding
import mrandroid.mazaady.presentation.BindingFragment
import mrandroid.mazaady.presentation.SharedViewModel
import mrandroid.mazaady.presentation.bottomsheet.SearchFragmentArgs
import mrandroid.mazaady.presentation.dialog.LoadingDialog
import mrandroid.mazaady.util.showToast
import mrandroid.mazaady.util.spinnerAdapter
import mrandroid.mazaady.util.state.UiState

@AndroidEntryPoint
class CategoriesFragment : BindingFragment<FragmentCategoriesBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentCategoriesBinding::inflate

    private val viewModel: CategoriesViewModel by viewModels()
    private val viewModelShared: SharedViewModel by activityViewModels()
    private var propertySelection = PropertySelection.ProcessType
    private var categoriesData: CategoriesData? = null
    private var mainCatsList: List<String> = emptyList()
    private var subCatsList: List<String> = emptyList()
    private var processTypeList: List<String> = emptyList()
    private var brandList: List<String> = emptyList()
    private var transmissionTypeList: List<String> = emptyList()
    private var fuelList: List<String> = emptyList()
    private var conditionList: List<String> = emptyList()
    private var colorList: List<String> = emptyList()
    private var odometerList: List<String> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFields()
        viewModel.getAllCats()

        binding.apply {

            inProcessType.at.setOnClickListener {
                propertySelection = PropertySelection.ProcessType
                findNavController().navigate(
                    R.id.searchFragment,
                    SearchFragmentArgs(processTypeList.toTypedArray(), "Process Type").toBundle()
                )
            }

            inBrand.at.setOnClickListener {
                propertySelection = PropertySelection.Brand
                findNavController().navigate(
                    R.id.searchFragment,
                    SearchFragmentArgs(brandList.toTypedArray(), "Brand").toBundle()
                )
            }

            inTransmissionType.at.setOnClickListener {
                propertySelection = PropertySelection.TransmissionType
                findNavController().navigate(
                    R.id.searchFragment,
                    SearchFragmentArgs(
                        transmissionTypeList.toTypedArray(),
                        "Transmission Type"
                    ).toBundle()
                )
            }

            inFuelType.at.setOnClickListener {
                propertySelection = PropertySelection.FuelType
                findNavController().navigate(
                    R.id.searchFragment,
                    SearchFragmentArgs(fuelList.toTypedArray(), "Fuel Type").toBundle()
                )
            }

            inCondition.at.setOnClickListener {
                propertySelection = PropertySelection.Condition
                findNavController().navigate(
                    R.id.searchFragment,
                    SearchFragmentArgs(conditionList.toTypedArray(), "Condition").toBundle()
                )
            }

            inColor.at.setOnClickListener {
                propertySelection = PropertySelection.Color
                findNavController().navigate(
                    R.id.searchFragment,
                    SearchFragmentArgs(colorList.toTypedArray(), "Color").toBundle()
                )
            }

            inOdometer.at.setOnClickListener {
                propertySelection = PropertySelection.Odometer
                findNavController().navigate(
                    R.id.searchFragment,
                    SearchFragmentArgs(odometerList.toTypedArray(), "Odometer").toBundle()
                )
            }

        }

        fetchCategoriesState()
        fetchPropertiesState()
        fetchSearchResult()
    }

    private fun initFields() {
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
            inColor.itl.hint = "Color"
            inOdometer.itl.hint = "Odometer"

            inMainCategories.at.inputType = InputType.TYPE_NULL
            inSubCategories.at.inputType = InputType.TYPE_NULL
            inProcessType.at.inputType = InputType.TYPE_NULL
            inSpecifyHere.at.inputType = InputType.TYPE_NULL
            inBrand.at.inputType = InputType.TYPE_NULL
            inModel.at.inputType = InputType.TYPE_NULL
            inType.at.inputType = InputType.TYPE_NULL
            inTransmissionType.at.inputType = InputType.TYPE_NULL
            inFuelType.at.inputType = InputType.TYPE_NULL
            inCondition.at.inputType = InputType.TYPE_NULL
            inColor.at.inputType = InputType.TYPE_NULL
            inOdometer.at.inputType = InputType.TYPE_NULL
        }
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
                mainCatsList = data.categories.map { it.slug ?: "Empty text" }
                inMainCategories.at.setAdapter(spinnerAdapter(requireContext(), mainCatsList))
                inMainCategories.at.setOnItemClickListener { adapterView, view, index, l ->
                    subCatsList = data.categories[index].children.map { it.slug!! }
                    inSubCategories.at.setAdapter(spinnerAdapter(requireContext(), subCatsList))

                    viewModelShared.optionsResult["Main category"] = mainCatsList[index]
                    viewModelShared.optionsResult["Sub category"] = ""
                    inSubCategories.at.setText("")
                }

                inSubCategories.at.setOnItemClickListener { adapterView, view, index, l ->
                    val catId = data.categories[index].children[index].id!!
                    viewModel.getPropertiesByCatId(catId)

                    viewModelShared.optionsResult["Sub category"] = subCatsList[index]
                }
            }
        }
    }

    private fun fetchPropertiesState() {
        lifecycleScope.launchWhenStarted {
            viewModel.propertiesState.collect { state ->
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
                        processTypeList = state.data?.data?.get(0)?.options?.map { it.slug!! }!!
                        brandList = state.data.data[1].options.map { it.slug!! }
                        transmissionTypeList = state.data.data[2].options.map { it.slug!! }
                        fuelList = state.data.data[3].options.map { it.slug!! }
                        conditionList = state.data.data[4].options.map { it.slug!! }
                        colorList = state.data.data[5].options.map { it.slug!! }
                        odometerList = state.data.data[6].options.map { it.slug!! }
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun fetchSearchResult() {
        binding.apply {
            viewModelShared.selectionValue.observe(viewLifecycleOwner) { value ->
                when (propertySelection) {
                    PropertySelection.ProcessType -> inProcessType.at.setText(value)
                    PropertySelection.Brand -> inBrand.at.setText(value)
                    PropertySelection.TransmissionType -> inTransmissionType.at.setText(value)
                    PropertySelection.FuelType -> inFuelType.at.setText(value)
                    PropertySelection.Condition -> inCondition.at.setText(value)
                    PropertySelection.Color -> inColor.at.setText(value)
                    PropertySelection.Odometer -> inOdometer.at.setText(value)
                }
            }
        }
    }
}