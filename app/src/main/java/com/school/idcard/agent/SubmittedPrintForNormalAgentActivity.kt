package com.school.idcard.agent

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.school.idcard.R
import com.school.idcard.databinding.ActivitySubmittedPrintForNormalAgentBinding

class SubmittedPrintForNormalAgentActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySubmittedPrintForNormalAgentBinding
    private var schoolId:String ?=""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_submitted_print_for_normal_agent)

        binding.toolbar.fileName.text="Submitted For Print"
        binding.toolbar.backArrowBtn.setOnClickListener { finish() }

        schoolId=intent.getStringExtra("schoolId")


    }
}