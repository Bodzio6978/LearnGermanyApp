package com.gmail.Bodziowaty6978

import android.content.Context
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.gmail.Bodziowaty6978.Adapters.ViewPagerAdapter
import com.gmail.Bodziowaty6978.AuthFragments.LoginFragment
import com.gmail.Bodziowaty6978.AuthFragments.RegisterFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class AuthActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        if (isDarkThemeOn()) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.lightGrey)
        }

        viewPager = findViewById(R.id.auth_viewPager)
        tabLayout = findViewById(R.id.auth_tabLayout)


        setUpTabs()
    }

    private fun setUpTabs() {
        val adapter = ViewPagerAdapter(this)
        adapter.addFragment(LoginFragment(), "Sign In")
        adapter.addFragment(RegisterFragment(), "Sign Up")
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = adapter.getTitle(position)
        }.attach()
    }

    private fun Context.isDarkThemeOn(): Boolean {
        return resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
    }
}