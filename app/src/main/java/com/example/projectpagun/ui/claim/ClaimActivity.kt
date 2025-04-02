package com.example.projectpagun.ui.claim

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectpagun.adapter.ClaimStatusAdapter
import com.example.projectpagun.databinding.ActivityClaimBinding
import com.example.projectpagun.model.Claim
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ClaimActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClaimBinding
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClaimBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // โหลดข้อมูลทะเบียนรถที่ได้รับการอนุมัติ
        loadApprovedCarRegistrations()

        // ตั้งค่าการคลิกปุ่มยืนยันการเคลม
        binding.btnSubmitClaim.setOnClickListener {
            val registration = binding.spinnerCarRegistration.selectedItem.toString()
            val claimDescription = binding.etClaimDescription.text.toString()
            val claimDate = binding.etClaimDate.text.toString()

            if (registration.isNotEmpty() && claimDescription.isNotEmpty() && claimDate.isNotEmpty()) {
                submitClaim(registration, claimDescription, claimDate)
            } else {
                Toast.makeText(this, "กรุณากรอกข้อมูลทั้งหมด", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // ฟังก์ชันโหลดทะเบียนรถที่ได้รับการอนุมัติ
    private fun loadApprovedCarRegistrations() {
        val currentUser = auth.currentUser ?: return
        db.collection("insurance_requests")
            .whereEqualTo("status", "approved")
            .whereEqualTo("userId", currentUser.uid)
            .get()
            .addOnSuccessListener { result ->
                val registrations = mutableListOf<String>()
                for (document in result) {
                    val registration = document.getString("licensePlate") ?: ""
                    if (registration.isNotEmpty()) {
                        registrations.add(registration)
                    }
                }

                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, registrations)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerCarRegistration.adapter = adapter
            }
            .addOnFailureListener {
                Toast.makeText(this, "ไม่สามารถโหลดข้อมูลทะเบียนรถได้", Toast.LENGTH_SHORT).show()
            }
    }

    // ฟังก์ชันส่งข้อมูลการเคลม
    private fun submitClaim(registration: String, claimDescription: String, claimDate: String) {
        val currentUser = auth.currentUser ?: return

        val claimData = hashMapOf(
            "userId" to currentUser.uid,
            "licensePlate" to registration,
            "claimDescription" to claimDescription,
            "claimDate" to claimDate,
            "status" to "pending",
            "timestamp" to com.google.firebase.firestore.FieldValue.serverTimestamp()
        )

        db.collection("claims")
            .add(claimData)
            .addOnSuccessListener {
                Toast.makeText(this, "ส่งคำขอเคลมสำเร็จ", Toast.LENGTH_SHORT).show()
                loadClaimStatusForCurrentUser() // โหลดสถานะการเคลมใหม่หลังจากส่งคำขอ
            }
            .addOnFailureListener {
                Toast.makeText(this, "ส่งคำขอเคลมล้มเหลว", Toast.LENGTH_SHORT).show()
            }
    }

    // ฟังก์ชันโหลดสถานะการเคลมทั้งหมดของผู้ใช้
    private fun loadClaimStatusForCurrentUser() {
        val currentUser = auth.currentUser ?: return
        db.collection("claims")
            .whereEqualTo("userId", currentUser.uid)
            .get()
            .addOnSuccessListener { result ->
                val claims = result.mapNotNull { it.toObject(Claim::class.java) }
                val adapter = ClaimStatusAdapter(claims)
                binding.recyclerClaimStatus.layoutManager = LinearLayoutManager(this)
                binding.recyclerClaimStatus.adapter = adapter
            }
            .addOnFailureListener {
                Toast.makeText(this, "โหลดสถานะการเคลมล้มเหลว", Toast.LENGTH_SHORT).show()
            }
    }
}
