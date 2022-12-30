package mrandroid.mazaady.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mrandroid.mazaady.data.remote.MazaadyApi
import mrandroid.mazaady.data.remote.dto.CategoriesResponse
import mrandroid.mazaady.domain.repository.MazaadyRepository
import mrandroid.mazaady.util.state.ApiState
import mrandroid.mazaady.util.state.Resource
import mrandroid.mazaady.util.toResultFlow

class MazaadyRepositoryImpl(private val mazaadyApi: MazaadyApi) : MazaadyRepository {

    override suspend fun getAllCats(): Flow<Resource<CategoriesResponse>> {
        return flow {
            val result = toResultFlow { mazaadyApi.getAllCats() }
            result.collect {
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> {
                        emit(
                            Resource.Error(
                                message = it.message!!,
                                errorCode = it.errorCode,
                                errorBody = it.errorBody
                            )
                        )
                    }
                    is ApiState.TokenExpired -> emit(Resource.TokenExpired())
                    is ApiState.Success -> emit(Resource.Success(it.data))
                }
            }
        }
    }

}