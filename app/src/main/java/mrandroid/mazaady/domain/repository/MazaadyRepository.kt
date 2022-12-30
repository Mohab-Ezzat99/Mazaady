package mrandroid.mazaady.domain.repository

import kotlinx.coroutines.flow.Flow
import mrandroid.mazaady.data.remote.dto.CategoriesResponse
import mrandroid.mazaady.util.state.Resource

interface MazaadyRepository {

    suspend fun getAllCats(): Flow<Resource<CategoriesResponse>>

}