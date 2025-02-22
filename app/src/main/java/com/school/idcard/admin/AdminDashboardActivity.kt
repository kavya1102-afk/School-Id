package com.school.idcard.admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.school.idcard.R
import com.school.idcard.admin.fragment.AdminHomeFragment
import com.school.idcard.admin.fragment.AdminStaffFragment
import com.school.idcard.databinding.ActivityAdminDashboardBinding

@Suppress("DEPRECATION")
class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAdminDashboardBinding
    private val fragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_admin_dashboard)

        binding.bottomNavigation.selectedItemId = R.id.nav_home

        setupClickListener()

        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.frameLayout, AdminHomeFragment())
        fragmentTransaction.commit()

        loadFragment(AdminHomeFragment())


        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    private fun setupClickListener() {
        binding.bottomNavigation.setOnItemSelectedListener {
            val fragment: Fragment = when (it.itemId) {
                R.id.nav_staff -> AdminStaffFragment()
                else -> AdminHomeFragment()
            }

            loadFragment(fragment)
            true
        }
    }


    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}