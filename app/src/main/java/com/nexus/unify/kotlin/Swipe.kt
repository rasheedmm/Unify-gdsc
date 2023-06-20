package com.live.newvideocall.kotlin

import android.net.Uri
import android.view.animation.AccelerateInterpolator
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLink
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.iosParameters
import com.google.firebase.dynamiclinks.ktx.shortLinkAsync
import com.google.firebase.ktx.Firebase
import com.yuyakaido.android.cardstackview.*
import com.google.firebase.dynamiclinks.ktx.component1
import com.google.firebase.dynamiclinks.ktx.component2
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
    fun getUrl(){

        val dynamicLink = Firebase.dynamicLinks.dynamicLink {
            link = Uri.parse("https://www.example.com/")
            domainUriPrefix = "https://example.page.link"
            // Open links with this app on Android
            androidParameters { }
            // Open links with com.example.ios on iOS
            iosParameters("com.example.ios") { }
        }

        val dynamicLinkUri = dynamicLink.uri

        val shortLinkTask = Firebase.dynamicLinks.shortLinkAsync {
            link = Uri.parse("https://www.example.com/")
            domainUriPrefix = "https://bookease.page.link"
            // Set parameters
            // ...
        }.addOnSuccessListener { (shortLink, flowchartLink) ->


            // Short link created

        }.addOnFailureListener {
            // Error
            // ...
        }
    }

}