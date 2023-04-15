package com.live.newvideocall.kotlin

import android.view.animation.AccelerateInterpolator
import com.yuyakaido.android.cardstackview.*


class Swipe {

    fun setData(card: CardStackView,manager: CardStackLayoutManager,direction: Direction){

        val setting = SwipeAnimationSetting.Builder()

            .setDirection(direction)
            .setDuration(1500)
            .setInterpolator(AccelerateInterpolator())

            .build()
        manager.setSwipeAnimationSetting(setting)
        card.swipe()

    }

}