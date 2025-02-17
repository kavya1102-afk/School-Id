package com.school.idcard

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import com.school.idcard.agent.PremiumAgentDashboard
import com.school.idcard.databinding.ActivityMainBinding
import com.school.idcard.network.SharedPrefManager
import com.school.idcard.other.LoginPage
import com.school.idcard.superadmin.SuperAdminDashboard

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPrefManager: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding=DataBindingUtil.setContentView(this, R.layout.activity_main)
        sharedPrefManager=SharedPrefManager(this)
        Handler(Looper.getMainLooper()).postDelayed({
           val token=sharedPrefManager.getToken()
           val role=sharedPrefManager.getRole()

            if(token=="" || token.isNullOrEmpty()){
                val intent = Intent(this, LoginPage::class.java)
                startActivity(intent)
            }else{
                when (role) {
                    "SUPERADMIN" -> {
                        startActivity(Intent(this,SuperAdminDashboard::class.java))
                    }
                    "PREMIUM" -> {
                        startActivity(Intent(this,PremiumAgentDashboard::class.java))
                    }
                    "NORMAL" -> {
                        startActivity(Intent(this,PremiumAgentDashboard::class.java))
                    }
                    else -> {
                        Toast.makeText(this,"Coming Soon", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }, 3000)



    }
}