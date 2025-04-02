package com.example.projectpagun.ui.claim

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectpagun.databinding.ActivityAdminClaimBinding
import com.google.firebase.firestore.FirebaseFirestore

class AdminClaimActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminClaimBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminClaimBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // โหลดข้อมูลคำร้องเคลมทั้งหมด
        loadClaims()
    }

    private fun loadClaims() {
        db.collection("claims")
            .get()
            .addOnSuccessListener { result ->
                val claimsList = mutableListOf<Claim>()
                for (document in result) {
                    val claim = document.toObject(Claim::class.java)
                    claim.id = document.id
                    claimsList.add(claim)
                }

                val adapter = ClaimAdapter(claimsList) { claim, newStatus ->
                    updateClaimStatus(claim, newStatus)
                }

                binding.recyclerClaims.layoutManager = LinearLayoutManager(this)
                binding.recyclerClaims.adapter = adapter
            }
            .addOnFailureListener {
                Toast.makeText(this, "ไม่สามารถดึงข้อมูลคำร้องได้", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateClaimStatus(claim: Claim, newStatus: String) {
        db.collection("claims").document(claim.id)
            .update("status", newStatus)
            .addOnSuccessListener {
                Toast.makeText(this, "อัปเดตสถานะสำเร็จเป็น $newStatus", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "อัปเดตสถานะล้มเหลว", Toast.LENGTH_SHORT).show()
            }
    }
}
