package com.example.projectpagun.ui.claim

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projectpagun.databinding.FragmentClaimBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.projectpagun.R


class ClaimFragment : Fragment() {

    private var _binding: FragmentClaimBinding? = null
    private val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentClaimBinding.inflate(inflater, container, false)

        // ปุ่มส่งเอกสารเคลม
        binding.btnSubmitClaim.setOnClickListener {
            submitClaim()
        }

        return binding.root
    }

    private fun submitClaim() {
        // ตรวจสอบข้อมูลที่กรอก
        val claimName = binding.etClaimName.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val claimReason = binding.etClaimReason.text.toString().trim()
        val claimDate = binding.etDateOfIncident.text.toString().trim()
        val carBrand = binding.etCarBrand.text.toString().trim()
        val carModel = binding.etCarModel.text.toString().trim()
        val carYear = binding.etCarYear.text.toString().trim()
        val licensePlate = binding.etLicensePlate.text.toString().trim()

        if (claimName.isEmpty() || phone.isEmpty() || email.isEmpty() || claimReason.isEmpty() || claimDate.isEmpty()) {
            Toast.makeText(requireContext(), "กรุณากรอกข้อมูลให้ครบถ้วน", Toast.LENGTH_SHORT).show()
            return
        }

        // สร้าง HashMap เพื่อเก็บข้อมูลการเคลม
        val claimData = hashMapOf(
            "userId" to user?.uid,
            "claimName" to claimName,
            "phone" to phone,
            "email" to email,
            "claimReason" to claimReason,
            "claimDate" to claimDate,
            "carBrand" to carBrand,
            "carModel" to carModel,
            "carYear" to carYear,
            "licensePlate" to licensePlate,
            "status" to "Pending"  // สถานะเริ่มต้นคือ Pending
        )

        // บันทึกข้อมูลการเคลมลง Firestore
        db.collection("insurance_requests")
            .add(claimData)
            .addOnSuccessListener {
                // ถ้าบันทึกสำเร็จ ให้ไปที่หน้าสถานะ
                Toast.makeText(requireContext(), "ส่งเอกสารเคลมสำเร็จ", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_claimFragment_to_statusFragment)
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "เกิดข้อผิดพลาดในการส่งเอกสารเคลม", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
