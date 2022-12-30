package mrandroid.mazaady.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CategoriesResponse(
    @SerializedName("code") var code: Int? = null,
    @SerializedName("msg") var msg: String? = null,
    @SerializedName("data") var data: CategoriesData? = CategoriesData()
)

data class Children(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("slug") var slug: String? = null,
    @SerializedName("children") var children: String? = null,
    @SerializedName("circle_icon") var circleIcon: String? = null,
    @SerializedName("disable_shipping") var disableShipping: Int? = null
)

data class Categories(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("slug") var slug: String? = null,
    @SerializedName("children") var children: ArrayList<Children> = arrayListOf(),
    @SerializedName("circle_icon") var circleIcon: String? = null,
    @SerializedName("disable_shipping") var disableShipping: Int? = null
)

data class Statistics(
    @SerializedName("auctions") var auctions: Int? = null,
    @SerializedName("users") var users: Int? = null,
    @SerializedName("products") var products: Int? = null
)

data class AdsBanners(
    @SerializedName("img") var img: String? = null,
    @SerializedName("media_type") var mediaType: String? = null,
    @SerializedName("duration") var duration: Int? = null
)

data class CategoriesData(
    @SerializedName("categories") var categories: ArrayList<Categories> = arrayListOf(),
    @SerializedName("statistics") var statistics: Statistics? = Statistics(),
    @SerializedName("ads_banners") var adsBanners: ArrayList<AdsBanners> = arrayListOf(),
    @SerializedName("ios_version") var iosVersion: String? = null,
    @SerializedName("google_version") var googleVersion: String? = null,
    @SerializedName("huawei_version") var huaweiVersion: String? = null
)