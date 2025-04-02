package com.example.projectpagun.ui.claim

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectpagun.adapter.ClaimStatusAdapter
import com.example.projectpagun.databinding.FragmentClaimBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.projectpagun.model.Claim

class ClaimFragment : Fragment() {

    private lateinit var binding: FragmentClaimBinding
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClaimBinding.inflate(inflater, container, false)

        // ตรวจสอบว่า User ล็อกอินอยู่หรือไม่
        val user = auth.currentUser
        if (user == null) {
            // ถ้าไม่พบ User ที่ล็อกอิน จะแสดงข้อความ
            Toast.makeText(requireContext(), "กรุณาล็อกอินก่อนใช้งาน", Toast.LENGTH_SHORT).show()
            return binding.root
        }

        // โหลดข้อมูลทะเบียนรถที่ได้รับการอนุมัติสำหรับ User นี้
        loadApprovedCarRegistrations(user.uid)

        // ตั้งค่าการคลิกปุ่มยืนยันการเคลม
        binding.btnSubmitClaim.setOnClickListener {
            val registration = binding.spinnerCarRegistration.selectedItem.toString()
            val claimDescription = binding.etClaimDescription.text.toString()
            val claimDate = binding.etClaimDate.text.toString()

            if (registration.isNotEmpty() && claimDescription.isNotEmpty() && claimDate.isNotEmpty()) {
                submitClaim(registration, claimDescription, claimDate)
            } else {
                Toast.makeText(requireContext(), "กรุณากรอกข้อมูลทั้งหมด", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    // ฟังก์ชันโหลดทะเบียนรถที่ได้รับการอนุมัติสำหรับ User นี้
    private fun loadApprovedCarRegistrations(userId: String) {
        db.collection("insurance_requests")
            .whereEqualTo("status", "approved")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                val registrations = mutableListOf<String>()
                for (document in result) {
                    val registration = document.getString("licensePlate") ?: ""
                    if (registration.isNotEmpty()) {
                        registrations.add(registration)
                    }
                }

                // ตั้งค่า Spinner ให้เลือกทะเบียนที่อนุมัติแล้ว
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, registrations)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerCarRegistration.adapter = adapter
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "ไม่สามารถโหลดข้อมูลทะเบียนรถได้", Toast.LENGTH_SHORT).show()
            }
    }

    // ฟังก์ชันส่งข้อมูลการเคลม
    private fun submitClaim(registration: String, claimDescription: String, claimDate: String) {
        val claimData = hashMapOf(
            "registration" to registration,
            "claimDescription" to claimDescription,
            "claimDate" to claimDate,
            "status" to "pending"
        )

        // เพิ่มข้อมูลการเคลมไปที่ Firestore
        db.collection("claims")
            .add(claimData)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "ส่งคำขอเคลมสำเร็จ", Toast.LENGTH_SHORT).show()
                loadClaimStatusForCurrentUser()  // โหลดใหม่หลังจากส่งเคลม
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "ส่งคำขอเคลมล้มเหลว", Toast.LENGTH_SHORT).show()
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
                binding.recyclerClaimStatus.layoutManager = LinearLayoutManager(requireContext())
                binding.recyclerClaimStatus.adapter = adapter  // ตั้งค่า adapter ให้กับ RecyclerView
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "โหลดสถานะการเคลมล้มเหลว", Toast.LENGTH_SHORT).show()
            }
    }
}
