package mrandroid.mazaady.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mrandroid.mazaady.data.remote.dto.Categories
import mrandroid.mazaady.data.remote.dto.CategoriesData
import mrandroid.mazaady.data.remote.dto.CategoriesResponse
import mrandroid.mazaady.data.remote.dto.PropertiesResponse
import mrandroid.mazaady.domain.repository.MazaadyRepository
import mrandroid.mazaady.util.state.Resource

class FakeMazaadyRepository : MazaadyRepository {

    private val catsResponse = CategoriesResponse(123, "msg", CategoriesData())
    private val propertiesResponse = PropertiesResponse(567, "msg2", arrayListOf())
    private val optionsResponse = PropertiesResponse(89, "msg3", arrayListOf())

    override suspend fun getAllCats(): Flow<Resource<CategoriesResponse>> {
        return flow {
            emit(Resource.Success(catsResponse))
        }
    }

    override suspend fun getPropertiesByCatId(catId: Int): Flow<Resource<PropertiesResponse>> {
        return flow {
            emit(Resource.Success(propertiesResponse))
        }
    }

    override suspend fun getOptionsBySubId(subId: Int): Flow<Resource<PropertiesResponse>> {
        return flow {
            emit(Resource.Success(optionsResponse))
        }
    }
}