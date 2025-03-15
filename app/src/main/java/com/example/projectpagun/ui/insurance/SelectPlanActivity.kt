package com.example.projectpagun.ui.insurance

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.projectpagun.R

class SelectPlanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_plan)

        // ❌ ไม่ต้องใช้ Activity ถ้า Fragment ใช้ Navigation
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SelectPlanFragment())
                .commit()
        }
    }
}
