package com.nexus.unify



import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database

import com.google.firebase.ktx.Firebase
import com.nexus.unify.AdapterClasses.AddPosts
import com.nexus.unify.AdapterClasses.ViewPagerAdapter
import com.nexus.unify.ModelClasses.Posts
import com.nexus.unify.databinding.ActivityPlayerBinding


class PlayerActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private var pos = 1;
    private var TYP = "MainActivity"
    lateinit var mainViewBinding: ActivityPlayerBinding
    private lateinit var database: DatabaseReference
    private val RECORD_REQUEST_CODE: Int = 101

    lateinit var pagerAdapter: FragmentStateAdapter

    lateinit var videoList: ArrayList<Posts>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewBinding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(mainViewBinding.root)

        setupPermissions()
        mainViewBinding.btnReqPermission.setOnClickListener { makeRequest() }


    }

    private fun initViewPagerView() {

        initializeList()
        database = Firebase.database.reference
        pagerAdapter = ViewPagerAdapter(this, videoList)
        mainViewBinding.viewPager.adapter = pagerAdapter
        mainViewBinding.viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL

    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, getString(R.string.permission_denied))
            makeRequest()
        } else {
            initViewPagerView()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            RECORD_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RECORD_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this,
                        getString(R.string.request_permission),
                        Toast.LENGTH_SHORT
                    ).show()
                    mainViewBinding.btnReqPermission.visibility = View.VISIBLE
                } else {
                    Log.i(TAG, getString(R.string.permission_granted))
                    mainViewBinding.btnReqPermission.visibility = View.GONE
                    initViewPagerView()
                }
            }
        }
    }

    private fun initializeList() {
        videoList = java.util.ArrayList<Posts>()
        val applicationContext = applicationContext
        val sharedPreference = getSharedPreferences("MY", Context.MODE_PRIVATE)
        pos = sharedPreference.getInt("pos", 0)-1


        // Toast.makeText(this, TYP, Toast.LENGTH_SHORT).show()

        sharedPreference.getLong("l", 1L)
        TYP = sharedPreference.getString("name", "")!!
        AddPosts.addposts(videoList, applicationContext,TYP)
 }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed(Runnable { mainViewBinding.viewPager.setCurrentItem(pos, false) }, 1)
    }

}