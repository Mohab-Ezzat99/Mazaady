package mrandroid.mazaady.presentation.categories

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mrandroid.mazaady.data.remote.dto.CategoriesResponse
import mrandroid.mazaady.data.remote.dto.PropertiesResponse
import mrandroid.mazaady.domain.repository.MazaadyRepository
import mrandroid.mazaady.util.state.Resource
import mrandroid.mazaady.util.state.UiState
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val repository: MazaadyRepository
) : ViewModel() {

    private val _categoriesState = MutableStateFlow<UiState<CategoriesResponse>>(UiState.Empty())
    val categoriesState: StateFlow<UiState<CategoriesResponse>> = _categoriesState

    private val _propertiesState = MutableStateFlow<UiState<PropertiesResponse>>(UiState.Empty())
    val propertiesState: StateFlow<UiState<PropertiesResponse>> = _propertiesState

    private val _optionsState = MutableStateFlow<UiState<PropertiesResponse>>(UiState.Empty())
    val optionsState: StateFlow<UiState<PropertiesResponse>> = _optionsState

    private var categoriesJob: Job? = null
    private var propertiesJob: Job? = null
    private var optionsJob: Job? = null

    fun cancelRequest() {
        categoriesJob?.cancel()
        propertiesJob?.cancel()
        optionsJob?.cancel()
    }

    fun getAllCats() {
        categoriesJob?.cancel()
        categoriesJob = viewModelScope.launch {
            withContext(coroutineContext) {
                repository.getAllCats().collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _categoriesState.value = UiState.Success(result.data)
                        }
                        is Resource.Error -> {
                            _categoriesState.value =
                                UiState.Error(result.message!!)
                        }
                        is Resource.TokenExpired -> _categoriesState.value = UiState.TokenExpired()
                        is Resource.Loading -> {
                            _categoriesState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }

    fun getPropertiesByCatId(catId: Int) {
        propertiesJob?.cancel()
        propertiesJob = viewModelScope.launch {
            withContext(coroutineContext) {
                repository.getPropertiesByCatId(catId).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _propertiesState.value = UiState.Success(result.data)
                        }
                        is Resource.Error -> {
                            _propertiesState.value =
                                UiState.Error(result.message!!)
                        }
                        is Resource.TokenExpired -> _propertiesState.value = UiState.TokenExpired()
                        is Resource.Loading -> {
                            _propertiesState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }

    fun getOptionsBySubId(subId: Int) {
        optionsJob?.cancel()
        optionsJob = viewModelScope.launch {
            withContext(coroutineContext) {
                repository.getOptionsBySubId(subId).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _optionsState.value = UiState.Success(result.data)
                        }
                        is Resource.Error -> {
                            _optionsState.value = UiState.Error(result.message!!)
                        }
                        is Resource.TokenExpired -> _optionsState.value = UiState.TokenExpired()
                        is Resource.Loading -> {
                            _optionsState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }

}