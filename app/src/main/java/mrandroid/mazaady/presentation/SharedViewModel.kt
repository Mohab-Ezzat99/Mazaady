package mrandroid.mazaady.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import mrandroid.mazaady.presentation.categories.PropertySelection
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {

    val selectionValue = MutableLiveData<String>()

}