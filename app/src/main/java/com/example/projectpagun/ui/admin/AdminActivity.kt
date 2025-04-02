package com.example.projectpagun.ui.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.projectpagun.LoginActivity
import com.example.projectpagun.R
import com.example.projectpagun.ui.admin.addplan.AddPlanActivity
import com.example.projectpagun.ui.claim.AdminClaimActivity
import com.google.firebase.auth.FirebaseAuth

class AdminActivity : AppCompatActivity() {

    private lateinit var btnCheckRequests: Button
    private lateinit var btnLogoutAdmin: Button
    private lateinit var btnAddPlan: Button
    private lateinit var btnClaimManagement: Button // ✅ ปุ่มใหม่สำหรับจัดการเคลม

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        // ✅ ผูกปุ่มกับ layout
        btnCheckRequests = findViewById(R.id.btnCheckRequests)
        btnLogoutAdmin = findViewById(R.id.btnLogoutAdmin)
        btnAddPlan = findViewById(R.id.btnAddPlan)
        btnClaimManagement = findViewById(R.id.btnClaimManagement) // 🆕 ผูกปุ่มใหม่

        // ✅ เปิดหน้ารายการคำขอซื้อประกัน
        btnCheckRequests.setOnClickListener {
            val intent = Intent(this, ClaimListActivity::class.java)
            startActivity(intent)
        }

        // ✅ ไปหน้าเพิ่มแผนประกัน
        btnAddPlan.setOnClickListener {
            val intent = Intent(this, AddPlanActivity::class.java)
            startActivity(intent)
        }

        // ✅ ไปหน้าจัดการเคลม
        btnClaimManagement.setOnClickListener {
            val intent = Intent(this, AdminClaimActivity::class.java)
            startActivity(intent)
        }

        // ✅ ออกจากระบบ
        btnLogoutAdmin.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
