package com.nexus.unify

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.component1
import com.google.firebase.dynamiclinks.ktx.component2
import com.google.firebase.dynamiclinks.ktx.dynamicLink
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.iosParameters
import com.google.firebase.dynamiclinks.ktx.shortLinkAsync
import com.google.firebase.ktx.Firebase

class UrlActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_url)
        Toast.makeText(this@UrlActivity, "thudangu", Toast.LENGTH_SHORT).show()
        val dynamicLink = Firebase.dynamicLinks.dynamicLink {
            link = Uri.parse("https://www.example.com/")
            domainUriPrefix = "https://bookease.page.link"
            // Open links with this app on Android
            androidParameters { }
            // Open links with com.example.ios on iOS
            iosParameters("com.example.ios") { }
        }

        val dynamicLinkUri = dynamicLink.uri


       Firebase.dynamicLinks.shortLinkAsync {
            link = (dynamicLinkUri)
            domainUriPrefix = "https://bookease.page.link"
            // Set parameters
            // ...
        }.addOnSuccessListener { (shortLink, flowchartLink) ->
            Toast.makeText(this@UrlActivity, ""+shortLink, Toast.LENGTH_SHORT).show()


            // Short link created

        }.addOnFailureListener {

            // ...
        }
      // Toast.makeText(this@UrlActivity, "ex"+shortLinkTask.exception, Toast.LENGTH_SHORT).show()

    }
}