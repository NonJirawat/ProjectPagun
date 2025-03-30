package com.example.projectpagun

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projectpagun.databinding.ActivityLoginBinding
import com.example.projectpagun.ui.Admin.AdminActivity
import com.example.projectpagun.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // ถ้าผู้ใช้เคยล็อกอินแล้ว ให้เข้าไปที่หน้า MainActivity ทันที
        if (auth.currentUser != null) {
            navigateToAppropriateActivity() // ถ้ามีผู้ใช้ล็อกอินอยู่แล้ว ไปที่หน้าเหมาะสม
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            // ตรวจสอบว่าอีเมลและรหัสผ่านไม่เป็นค่าว่าง
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "กรุณากรอกอีเมลและรหัสผ่าน", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ทำการเข้าสู่ระบบด้วย Firebase Authentication
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // ถ้าล็อกอินสำเร็จ ให้ไปตรวจสอบ role จาก Firebase
                        val userId = auth.currentUser?.uid
                        val database = FirebaseDatabase.getInstance().getReference("users").child(userId!!)

                        // ดึงข้อมูล role จาก Firebase
                        database.get().addOnSuccessListener { snapshot ->
                            val role = snapshot.child("role").value?.toString()
                            if (role != null) {
                                navigateToAppropriateActivity(role)
                            } else {
                                Toast.makeText(this, "ไม่พบบทบาทของผู้ใช้ในระบบ", Toast.LENGTH_SHORT).show()
                            }
                        }.addOnFailureListener {
                            Toast.makeText(this, "เกิดข้อผิดพลาดในการดึงข้อมูลบทบาทของผู้ใช้", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // หากเข้าสู่ระบบไม่สำเร็จ
                        Toast.makeText(this, "เข้าสู่ระบบล้มเหลว: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        binding.tvRegister.setOnClickListener {
            // ถ้าผู้ใช้ยังไม่ได้สมัครสมาชิก, ให้ไปหน้า RegisterActivity
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun navigateToAppropriateActivity(role: String? = null) {
        // ตรวจสอบบทบาทของผู้ใช้แล้วไปหน้าเหมาะสม
        if (role == "admin") {
            startActivity(Intent(this, AdminActivity::class.java))  // ไปหน้า AdminActivity ถ้าเป็น admin
        } else {
            startActivity(Intent(this, MainActivity::class.java))  // ไปหน้า MainActivity ถ้าเป็น user
        }
        finish()  // ปิดหน้า LoginActivity หลังจากไปหน้าใหม่
    }
}
