package mrandroid.mazaady.data.remote

import mrandroid.mazaady.data.remote.dto.CategoriesResponse
import mrandroid.mazaady.data.remote.dto.PropertiesResponse
import retrofit2.Response
import retrofit2.http.*

interface MazaadyApi {
    companion object {
        const val BASE_URL = "https://staging.mazaady.com/"
    }

    @GET("api/get_all_cats")
    suspend fun getAllCats(): Response<CategoriesResponse>

    @GET("api/properties")
    suspend fun getPropertiesByCatId(@Query("cat") catId: Int): Response<PropertiesResponse>

}