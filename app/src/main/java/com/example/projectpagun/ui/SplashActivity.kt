package com.example.projectpagun

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash) // layout ที่มีโลโก้

        Handler(Looper.getMainLooper()).postDelayed({

            // ✅ เช็คสถานะล็อกอิน
            val user = FirebaseAuth.getInstance().currentUser

            if (user != null) {
                // ถ้ายังมี user แปลว่า Login อยู่ → ไปหน้า MainActivity
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                // ถ้าไม่มี user → ไปหน้า LoginActivity
                startActivity(Intent(this, LoginActivity::class.java))
            }

            finish()
        }, 2500) // 2.5 วินาที
    }
}