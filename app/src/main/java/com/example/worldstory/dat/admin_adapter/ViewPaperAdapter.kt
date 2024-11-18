package com.example.worldstory.dat.admin_adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.worldstory.dat.admin_view_navs.CommentFragment
import com.example.worldstory.dat.admin_view_navs.chapter_activity.ChapterFragment
import com.example.worldstory.dat.admin_view_navs.chapter_activity.RateFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val fragments = listOf(
       ChapterFragment(),
        CommentFragment(),
       RateFragment()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}