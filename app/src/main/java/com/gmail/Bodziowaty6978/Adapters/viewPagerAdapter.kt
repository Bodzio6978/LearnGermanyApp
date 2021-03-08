package com.gmail.Bodziowaty6978.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter

class viewPagerAdapter(supportFragmentManager: FragmentActivity) : FragmentStateAdapter(supportFragmentManager) {

    private val mFragmentList = ArrayList<Fragment>()
    private val mFragmentTitleList = ArrayList<String>()

    override fun getItemCount(): Int {
        return mFragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return mFragmentList[position]
    }
    fun getTitle(position: Int): String{
        return mFragmentTitleList[position]
    }

    fun addFragment(fragment : Fragment, title : String){
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }
}