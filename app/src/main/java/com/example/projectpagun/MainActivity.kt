package com.example.projectpagun

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.projectpagun.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import androidx.core.view.WindowCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        // เชื่อมโยงกับ BottomNavigationView
        navView.setupWithNavController(navController)

        // ทดสอบการส่งข้อมูลไปยัง Firebase
        sendMessageToFirebase("Hello Firebase!")

        // ตั้งค่าการนำทางเมื่อผู้ใช้เลือกแท็บใน BottomNavigationView
        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    navController.navigate(R.id.navigation_home)
                    true
                }
                R.id.navigation_status -> {
                    navController.navigate(R.id.navigation_status)
                    true
                }
                R.id.navigation_notifications -> {
                    navController.navigate(R.id.navigation_notifications)
                    true
                }
                else -> false
            }
        }
    }

    // ฟังก์ชันเพื่อส่งข้อความไปยัง Firebase
    private fun sendMessageToFirebase(message: String) {
        database = FirebaseDatabase.getInstance().getReference("messages")
        database.setValue(message)
            .addOnSuccessListener {
                Log.d("FirebaseTest", "✅ ส่งข้อมูลไปยัง Firebase สำเร็จ!")
            }
            .addOnFailureListener {
                Log.e("FirebaseTest", "❌ ส่งข้อมูลล้มเหลว: ${it.message}")
            }
    }
}
