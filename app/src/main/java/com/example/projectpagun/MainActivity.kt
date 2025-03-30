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
    private lateinit var database: DatabaseReference // Firebase Database Reference

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        // ❌ เอา action ออกจาก AppBarConfiguration เพราะมันไม่ใช่ destination
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_notifications
            )
        )



        navView.setupWithNavController(navController)

        // ✅ ทดสอบส่งข้อมูลไป Firebase
        database = FirebaseDatabase.getInstance().getReference("messages")
        database.setValue("Hello Firebase!")
            .addOnSuccessListener {
                Log.d("FirebaseTest", "✅ ส่งข้อมูลไปยัง Firebase สำเร็จ!")
            }
            .addOnFailureListener {
                Log.e("FirebaseTest", "❌ ส่งข้อมูลล้มเหลว: ${it.message}")
            }
    }
}
