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

    private var categoriesJob: Job? = null

    fun cancelRequest() {
        categoriesJob?.cancel()
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

}