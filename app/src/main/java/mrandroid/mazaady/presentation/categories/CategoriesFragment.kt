package mrandroid.mazaady.presentation.categories

import android.os.Bundle
import android.text.InputType
import android.util.Log
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
import mrandroid.mazaady.data.remote.dto.Children
import mrandroid.mazaady.data.remote.dto.Options
import mrandroid.mazaady.databinding.FragmentCategoriesBinding
import mrandroid.mazaady.domain.model.ResultModel
import mrandroid.mazaady.presentation.BindingFragment
import mrandroid.mazaady.presentation.SharedViewModel
import mrandroid.mazaady.presentation.bottomsheet.SearchFragmentArgs
import mrandroid.mazaady.presentation.dialog.LoadingDialog
import mrandroid.mazaady.presentation.result.ResultFragmentArgs
import mrandroid.mazaady.util.Constants
import mrandroid.mazaady.util.Constants.TAG
import mrandroid.mazaady.util.showToast
import mrandroid.mazaady.util.spinnerAdapter
import mrandroid.mazaady.util.state.UiState

@AndroidEntryPoint
class CategoriesFragment : BindingFragment<FragmentCategoriesBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentCategoriesBinding::inflate

    private val viewModel: CategoriesViewModel by viewModels()
    private val viewModelShared: SharedViewModel by activityViewModels()
    private var propertySelection = PropertySelection.MainCat
    private var categoriesData: CategoriesData? = null
    private var currentMainChildren: List<Children>? = null
    private var currentBrandOptions: List<Options>? = null
    private var currentModelOptions: List<Options>? = null

    private var mainCatsList: List<String> = emptyList()
    private var subCatsList: List<String> = emptyList()
    private var processTypeList: List<String> = emptyList()
    private var brandList: List<String> = emptyList()
    private var modelList: List<String> = emptyList()
    private var typeList: List<String> = emptyList()
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

            inMainCategories.at.setOnClickListener {
                propertySelection = PropertySelection.MainCat
                findNavController().navigate(
                    R.id.searchFragment,
                    SearchFragmentArgs(
                        mainCatsList.toTypedArray(),
                        propertySelection.label
                    ).toBundle()
                )
            }

            inSubCategories.at.setOnClickListener {
                propertySelection = PropertySelection.SubCat
                findNavController().navigate(
                    R.id.searchFragment,
                    SearchFragmentArgs(
                        subCatsList.toTypedArray(),
                        propertySelection.label
                    ).toBundle()
                )
            }

            inProcessType.at.setOnClickListener {
                propertySelection = PropertySelection.ProcessType
                findNavController().navigate(
                    R.id.searchFragment,
                    SearchFragmentArgs(
                        processTypeList.toTypedArray(),
                        propertySelection.label
                    ).toBundle()
                )
            }

            inBrand.at.setOnClickListener {
                propertySelection = PropertySelection.Brand
                findNavController().navigate(
                    R.id.searchFragment,
                    SearchFragmentArgs(brandList.toTypedArray(), propertySelection.label).toBundle()
                )
            }

            inModel.at.setOnClickListener {
                propertySelection = PropertySelection.Model
                findNavController().navigate(
                    R.id.searchFragment,
                    SearchFragmentArgs(modelList.toTypedArray(), propertySelection.label).toBundle()
                )
            }

            inType.at.setOnClickListener {
                propertySelection = PropertySelection.Type
                findNavController().navigate(
                    R.id.searchFragment,
                    SearchFragmentArgs(typeList.toTypedArray(), propertySelection.label).toBundle()
                )
            }

            inTransmissionType.at.setOnClickListener {
                propertySelection = PropertySelection.TransmissionType
                findNavController().navigate(
                    R.id.searchFragment,
                    SearchFragmentArgs(
                        transmissionTypeList.toTypedArray(),
                        propertySelection.label
                    ).toBundle()
                )
            }

            inFuelType.at.setOnClickListener {
                propertySelection = PropertySelection.FuelType
                findNavController().navigate(
                    R.id.searchFragment,
                    SearchFragmentArgs(fuelList.toTypedArray(), propertySelection.label).toBundle()
                )
            }

            inCondition.at.setOnClickListener {
                propertySelection = PropertySelection.Condition
                findNavController().navigate(
                    R.id.searchFragment,
                    SearchFragmentArgs(
                        conditionList.toTypedArray(),
                        propertySelection.label
                    ).toBundle()
                )
            }

            inColor.at.setOnClickListener {
                propertySelection = PropertySelection.Color
                findNavController().navigate(
                    R.id.searchFragment,
                    SearchFragmentArgs(colorList.toTypedArray(), propertySelection.label).toBundle()
                )
            }

            inOdometer.at.setOnClickListener {
                propertySelection = PropertySelection.Odometer
                findNavController().navigate(
                    R.id.searchFragment,
                    SearchFragmentArgs(
                        odometerList.toTypedArray(),
                        propertySelection.label
                    ).toBundle()
                )
            }

            btnSubmit.setOnClickListener {
                collectResults()
            }

        }

        fetchCategoriesState()
        fetchPropertiesState()
        fetchOptionsState()
        fetchSearchResult()
    }

    private fun initFields() {
        binding.apply {
            inMainCategories.itl.hint = PropertySelection.MainCat.label
            inSubCategories.itl.hint = PropertySelection.SubCat.label
            inProcessType.itl.hint = PropertySelection.ProcessType.label
            inBrand.itl.hint = PropertySelection.Brand.label
            inModel.itl.hint = PropertySelection.Model.label
            inType.itl.hint = PropertySelection.Type.label
            inTransmissionType.itl.hint = PropertySelection.TransmissionType.label
            inFuelType.itl.hint = PropertySelection.FuelType.label
            inCondition.itl.hint = PropertySelection.Condition.label
            inColor.itl.hint = PropertySelection.Color.label
            inOdometer.itl.hint = PropertySelection.Odometer.label

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
                        categoriesData?.let { data ->
                            mainCatsList = data.categories.map { it.slug ?: "Empty text" }
                        }
                    }
                    else -> Unit
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
                        currentBrandOptions = state.data.data[1].options // brand options
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

    private fun fetchOptionsState() {
        lifecycleScope.launchWhenStarted {
            viewModel.optionsState.collect { state ->
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
                        when (propertySelection) {
                            PropertySelection.Brand -> { // update model options
                                currentModelOptions= state.data?.data?.get(0)?.options
                                modelList = state.data?.data?.get(0)?.options?.map { it.slug!! }!!
                            }
                            PropertySelection.Model -> { // update type options
                                typeList = state.data?.data?.get(0)?.options?.map { it.slug!! }!!
                            }
                            else -> Unit
                        }
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
                    PropertySelection.MainCat -> {
                        if (value.equals(Constants.OTHER))
                            inSpecifyMainCat.root.isVisible = true
                        else {
                            inMainCategories.at.setText(value)
                            inSpecifyMainCat.root.isVisible = false

                            // setup subCat list from mainCat list
                            val index = mainCatsList.indexOf(value)
                            currentMainChildren = categoriesData?.categories?.get(index)?.children
                            currentMainChildren?.let { children ->
                                subCatsList = children.map { it.slug!! }
                            }
                        }
                    }
                    PropertySelection.SubCat -> {
                        if (value.equals(Constants.OTHER))
                            inSpecifySubCat.root.isVisible = true
                        else {
                            inSubCategories.at.setText(value)
                            inSpecifySubCat.root.isVisible = false

                            // setup properties list from subCat list
                            val index = subCatsList.indexOf(value)
                            currentMainChildren?.let { children ->
                                viewModel.getPropertiesByCatId(children[index].id!!)
                            }
                        }
                    }
                    PropertySelection.ProcessType -> {
                        if (value.equals(Constants.OTHER))
                            inSpecifyProcess.root.isVisible = true
                        else {
                            inProcessType.at.setText(value)
                            inSpecifyProcess.root.isVisible = false
                        }
                    }
                    PropertySelection.Brand -> {
                        if (value.equals(Constants.OTHER))
                            inSpecifyBrand.root.isVisible = true
                        else {
                            inBrand.at.setText(value)
                            inSpecifyBrand.root.isVisible = false

                            // setup modelOptions list from brandOptions list
                            val index = brandList.indexOf(value)
                            currentBrandOptions?.let { options ->
                                viewModel.getOptionsBySubId(options[index].id!!)
                            }
                        }
                    }
                    PropertySelection.Model -> {
                        if (value.equals(Constants.OTHER))
                            inSpecifyModel.root.isVisible = true
                        else {
                            inModel.at.setText(value)
                            inSpecifyModel.root.isVisible = false

                            // setup typeOptions list from modelOptions list
                            val index = modelList.indexOf(value)
                            currentModelOptions?.let { options ->
                                viewModel.getOptionsBySubId(options[index].id!!)
                            }
                        }
                    }
                    PropertySelection.Type -> {
                        if (value.equals(Constants.OTHER))
                            inSpecifyType.root.isVisible = true
                        else {
                            inType.at.setText(value)
                            inSpecifyType.root.isVisible = false
                        }
                    }
                    PropertySelection.TransmissionType -> {
                        if (value.equals(Constants.OTHER))
                            inSpecifyTransmissionType.root.isVisible = true
                        else {
                            inTransmissionType.at.setText(value)
                            inSpecifyTransmissionType.root.isVisible = false
                        }
                    }
                    PropertySelection.FuelType -> {
                        if (value.equals(Constants.OTHER))
                            inSpecifyFuelType.root.isVisible = true
                        else {
                            inFuelType.at.setText(value)
                            inSpecifyFuelType.root.isVisible = false
                        }
                    }
                    PropertySelection.Condition -> {
                        if (value.equals(Constants.OTHER))
                            inSpecifyCondition.root.isVisible = true
                        else {
                            inCondition.at.setText(value)
                            inSpecifyCondition.root.isVisible = false
                        }
                    }
                    PropertySelection.Color -> {
                        if (value.equals(Constants.OTHER))
                            inSpecifyColor.root.isVisible = true
                        else {
                            inColor.at.setText(value)
                            inSpecifyColor.root.isVisible = false
                        }
                    }
                    PropertySelection.Odometer -> {
                        if (value.equals(Constants.OTHER))
                            inSpecifyOdometer.root.isVisible = true
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
            var mainCatValue = inMainCategories.at.text.toString().trim()
            if (mainCatValue.isEmpty()) mainCatValue = inSpecifyMainCat.et.text.toString().trim()

            var subCatValue = inSubCategories.at.text.toString().trim()
            if (subCatValue.isEmpty()) subCatValue = inSpecifySubCat.et.text.toString().trim()

            var processValue = inProcessType.at.text.toString().trim()
            if (processValue.isEmpty()) processValue = inSpecifyProcess.et.text.toString().trim()

            var brandValue = inBrand.at.text.toString().trim()
            if (brandValue.isEmpty()) brandValue = inSpecifyBrand.et.text.toString().trim()

            var modelValue = inModel.at.text.toString().trim()
            if (modelValue.isEmpty()) modelValue = inSpecifyModel.et.text.toString().trim()

            var typeValue = inType.at.text.toString().trim()
            if (typeValue.isEmpty()) typeValue = inSpecifyType.et.text.toString().trim()

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
            resultList.add(
                ResultModel(
                    title = PropertySelection.MainCat.label,
                    value = mainCatValue
                )
            )
            resultList.add(ResultModel(title = PropertySelection.SubCat.label, value = subCatValue))
            resultList.add(
                ResultModel(
                    title = PropertySelection.ProcessType.label,
                    value = processValue
                )
            )
            resultList.add(ResultModel(title = PropertySelection.Brand.label, value = brandValue))
            resultList.add(ResultModel(title = PropertySelection.Model.label, value = modelValue))
            resultList.add(ResultModel(title = PropertySelection.Type.label, value = typeValue))
            resultList.add(
                ResultModel(
                    title = PropertySelection.TransmissionType.label,
                    value = transmissionValue
                )
            )
            resultList.add(ResultModel(title = PropertySelection.FuelType.label, value = fuelValue))
            resultList.add(
                ResultModel(
                    title = PropertySelection.Condition.label,
                    value = conditionValue
                )
            )
            resultList.add(ResultModel(title = PropertySelection.Color.label, value = colorValue))
            resultList.add(
                ResultModel(
                    title = PropertySelection.Odometer.label,
                    value = odometerValue
                )
            )

            findNavController().navigate(
                R.id.resultFragment,
                ResultFragmentArgs(resultList.toTypedArray()).toBundle()
            )
        }
    }
}