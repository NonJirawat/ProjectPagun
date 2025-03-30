package com.example.projectpagun

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projectpagun.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import android.util.Patterns

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            // ตรวจสอบการเลือกบทบาท
            val selectedRole = when {
                binding.radioUser.isChecked -> "user"
                binding.radioAdmin.isChecked -> "admin"
                else -> {
                    Toast.makeText(this, "กรุณาเลือกบทบาท", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "กรุณากรอกอีเมลและรหัสผ่าน", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid
                        val database = FirebaseDatabase.getInstance().getReference("users")

                        // บันทึก role ใน Firebase Database
                        val userMap = mapOf("role" to selectedRole)

                        userId?.let {
                            database.child(it).setValue(userMap)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "สมัครสมาชิกสำเร็จ", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this, LoginActivity::class.java))
                                    finish()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "เกิดข้อผิดพลาดในการบันทึกบทบาทผู้ใช้", Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        Toast.makeText(this, "สมัครสมาชิกล้มเหลว: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
