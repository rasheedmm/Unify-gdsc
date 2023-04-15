package com.nexus.unify.AdapterClasses

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nexus.unify.Fragments.ExoPlayerFragment



import com.nexus.unify.ModelClasses.Posts
import com.nexus.unify.PlayerActivity


class ViewPagerAdapter(
    private val activity: PlayerActivity,
    private val videoList: ArrayList<Posts>
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return videoList.size
    }

    override fun createFragment(position: Int): Fragment {
        return ExoPlayerFragment(activity, videoList[position], position)
    }
}