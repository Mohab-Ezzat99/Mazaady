package mrandroid.mazaady.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PropertiesResponse(
    @SerializedName("code") var code: Int? = null,
    @SerializedName("msg") var msg: String? = null,
    @SerializedName("data") var data: ArrayList<PropertiesData> = arrayListOf()
)

data class Options(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("slug") var slug: String? = null,
    @SerializedName("parent") var parent: Int? = null,
    @SerializedName("child") var child: Boolean? = null
)

data class PropertiesData(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("slug") var slug: String? = null,
    @SerializedName("parent") var parent: String? = null,
    @SerializedName("list") var list: Boolean? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("value") var value: String? = null,
    @SerializedName("other_value") var otherValue: String? = null,
    @SerializedName("options") var options: ArrayList<Options> = arrayListOf()
)