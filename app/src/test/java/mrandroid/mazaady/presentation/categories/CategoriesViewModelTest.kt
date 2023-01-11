package mrandroid.mazaady.presentation.categories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import mrandroid.mazaady.MainDispatcherRule
import mrandroid.mazaady.data.repository.FakeMazaadyRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CategoriesViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: CategoriesViewModel

    @Before
    fun setup() {
        viewModel = CategoriesViewModel(FakeMazaadyRepository())
    }

    @Test
    fun getAllCats_returnsCatResponse() = runTest {
        viewModel.getAllCats()
        viewModel.categoriesState.test {
            awaitItem() // ignore default emit (UI Empty)
            val result = awaitItem().data
            assertThat(result).isNotNull()
        }
    }

    @Test
    fun getProperties_returnsPropertiesResponse() = runTest {
        viewModel.getPropertiesByCatId(13)
        viewModel.propertiesState.test {
            awaitItem() // ignore default emit (UI Empty)
            val result = awaitItem().data
            assertThat(result).isNotNull()
        }
    }

    @Test
    fun getOptions_returnsPropertiesResponse() = runTest {
        viewModel.getOptionsBySubId(53)
        viewModel.optionsState.test {
            awaitItem() // ignore default emit (UI Empty)
            val result = awaitItem().data
            assertThat(result).isNotNull()
        }
    }

}