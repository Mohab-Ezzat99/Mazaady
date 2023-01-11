package mrandroid.mazaady.domain.repository

import kotlinx.coroutines.flow.Flow
import mrandroid.mazaady.data.remote.dto.CategoriesResponse
import mrandroid.mazaady.data.remote.dto.PropertiesResponse
import mrandroid.mazaady.util.state.Resource
import retrofit2.Response
import retrofit2.http.Path

interface MazaadyRepository {

    suspend fun getAllCats(): Flow<Resource<CategoriesResponse>>

    suspend fun getPropertiesByCatId(catId: Int): Flow<Resource<PropertiesResponse>>

    suspend fun getOptionsBySubId(subId: Int): Flow<Resource<PropertiesResponse>>
}