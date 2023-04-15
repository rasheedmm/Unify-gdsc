package com.nexus.latticetkmce.application;
import android.app.Application


class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initExoPlayerCaching()
    }

    private fun initExoPlayerCaching() {

        val exoPlayerCaching = ExoPlayerCaching()
        exoPlayerCaching.init(this, StorageUtil.getExoCacheDir(this))

    }

}