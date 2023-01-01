package mrandroid.mazaady.presentation.categories

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import mrandroid.mazaady.R
import mrandroid.mazaady.data.remote.dto.CategoriesData
import mrandroid.mazaady.databinding.FragmentCategoriesBinding
import mrandroid.mazaady.domain.model.ResultModel
import mrandroid.mazaady.presentation.BindingFragment
import mrandroid.mazaady.presentation.SharedViewModel
import mrandroid.mazaady.presentation.bottomsheet.SearchFragmentArgs
import mrandroid.mazaady.presentation.dialog.LoadingDialog
import mrandroid.mazaady.presentation.result.ResultFragmentArgs
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

            btnSubmit.setOnClickListener {
                collectResults()
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
                    inSubCategories.at.setText("")
                }

                inSubCategories.at.setOnItemClickListener { adapterView, view, index, l ->
                    val catId = data.categories[index].children[index].id!!
                    viewModel.getPropertiesByCatId(catId)
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
                    PropertySelection.ProcessType -> {
                        if (value.equals("other")) inSpecifyProcess.root.isVisible = true
                        else {
                            inProcessType.at.setText(value)
                            inSpecifyProcess.root.isVisible = false
                        }
                    }
                    PropertySelection.Brand -> {
                        if (value.equals("other")) inSpecifyBrand.root.isVisible = true
                        else {
                            inBrand.at.setText(value)
                            inSpecifyBrand.root.isVisible = false
                        }
                    }
                    PropertySelection.TransmissionType -> {
                        if (value.equals("other")) inSpecifyTransmissionType.root.isVisible = true
                        else {
                            inTransmissionType.at.setText(value)
                            inSpecifyTransmissionType.root.isVisible = false
                        }
                    }
                    PropertySelection.FuelType -> {
                        if (value.equals("other")) inSpecifyFuelType.root.isVisible = true
                        else {
                            inFuelType.at.setText(value)
                            inSpecifyFuelType.root.isVisible = false
                        }
                    }
                    PropertySelection.Condition -> {
                        if (value.equals("other")) inSpecifyCondition.root.isVisible = true
                        else {
                            inCondition.at.setText(value)
                            inSpecifyCondition.root.isVisible = false
                        }
                    }
                    PropertySelection.Color -> {
                        if (value.equals("other")) inSpecifyColor.root.isVisible = true
                        else {
                            inColor.at.setText(value)
                            inSpecifyColor.root.isVisible = false
                        }
                    }
                    PropertySelection.Odometer -> {
                        if (value.equals("other")) inSpecifyOdometer.root.isVisible = true
                        else {
                            inOdometer.at.setText(value)
                            inSpecifyOdometer.root.isVisible = false
                        }
                    }
                }
            }
        }
    }

    private fun collectResults() {
        binding.apply {
            val mainCatValue = inMainCategories.at.text.toString().trim()
            val subCatValue = inSubCategories.at.text.toString().trim()

            var processValue = inProcessType.at.text.toString().trim()
            if (processValue.isEmpty()) processValue = inSpecifyProcess.et.text.toString().trim()

            var brandValue = inBrand.at.text.toString().trim()
            if (brandValue.isEmpty()) brandValue = inSpecifyBrand.et.text.toString().trim()

            var transmissionValue = inTransmissionType.at.text.toString().trim()
            if (transmissionValue.isEmpty()) transmissionValue =
                inSpecifyTransmissionType.et.text.toString().trim()

            var fuelValue = inFuelType.at.text.toString().trim()
            if (fuelValue.isEmpty()) fuelValue = inSpecifyFuelType.et.text.toString().trim()

            var conditionValue = inCondition.at.text.toString().trim()
            if (conditionValue.isEmpty()) conditionValue =
                inSpecifyCondition.et.text.toString().trim()

            var colorValue = inColor.at.text.toString().trim()
            if (colorValue.isEmpty()) colorValue = inSpecifyColor.et.text.toString().trim()

            var odometerValue = inOdometer.at.text.toString().trim()
            if (odometerValue.isEmpty()) odometerValue = inSpecifyOdometer.et.text.toString().trim()

            val resultList = ArrayList<ResultModel>()
            resultList.add(ResultModel(title = "Main Category", value = mainCatValue))
            resultList.add(ResultModel(title = "Sub Category", value = subCatValue))
            resultList.add(ResultModel(title = "Process Type", value = processValue))
            resultList.add(ResultModel(title = "Brand", value = brandValue))
            resultList.add(ResultModel(title = "Transmission Type", value = transmissionValue))
            resultList.add(ResultModel(title = "Fuel Type", value = fuelValue))
            resultList.add(ResultModel(title = "Condition", value = conditionValue))
            resultList.add(ResultModel(title = "Color", value = colorValue))
            resultList.add(ResultModel(title = "Odometer", value = odometerValue))

            findNavController().navigate(
                R.id.resultFragment,
                ResultFragmentArgs(resultList.toTypedArray()).toBundle()
            )
        }
    }
}