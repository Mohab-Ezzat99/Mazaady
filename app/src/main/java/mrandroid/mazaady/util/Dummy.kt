package mrandroid.mazaady.util

import mrandroid.mazaady.R
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

object Dummy {

    fun dummyList(): ArrayList<String> {
        val list = ArrayList<String>()
        for (i in 0..2) list.add(i.toString())
        return list
    }

    fun dummyBiddersList(): ArrayList<Int> {
        val list = ArrayList<Int>()
        list.add(R.drawable.pic_person1)
        list.add(R.drawable.pic_person2)
        list.add(R.drawable.pic_person3)
        return list
    }

    fun dummySliderList(): ArrayList<CarouselItem> {
        val list = ArrayList<CarouselItem>()
        list.add(CarouselItem(imageDrawable = R.drawable.pic_slider_img))
        list.add(CarouselItem(imageDrawable = R.drawable.pic_slider_img))
        list.add(CarouselItem(imageDrawable = R.drawable.pic_slider_img))
        return list
    }

}