package com.example.projectpagun.ui.insurance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projectpagun.databinding.FragmentConfirmPurchaseBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ConfirmPurchaseFragment : Fragment() {

    private var _binding: FragmentConfirmPurchaseBinding? = null
    private val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfirmPurchaseBinding.inflate(inflater, container, false)

        val args = arguments
        val planId = args?.getString("planId") ?: return binding.root
        val planTitle = args.getString("title") ?: ""
        val planPrice = args.getLong("price")

        binding.btnConfirmPurchase.setOnClickListener {
            val license = binding.etLicensePlate.text.toString().trim()
            val name = binding.etOwnerName.text.toString().trim()
            val idcard = binding.etIdCard.text.toString().trim()
            val egcode = binding.etEgCode.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()
            val address = binding.etAddress.text.toString().trim()

            if (license.isEmpty() || name.isEmpty() ||  idcard.isEmpty() || idcard.isEmpty() ||  phone.isEmpty() || address.isEmpty() ) {
                Toast.makeText(requireContext(), "กรุณากรอกข้อมูลให้ครบถ้วน", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = FirebaseAuth.getInstance().currentUser
            if (user == null) {
                Toast.makeText(requireContext(), "กรุณาเข้าสู่ระบบก่อน", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val purchaseData = hashMapOf(
                "userId" to user.uid,
                "planId" to planId,
                "licensePlate" to license,
                "ownerName" to name,
                "idcard" to idcard,
                "egcode" to egcode,
                "phone" to phone,
                "address" to address,
                "status" to "pending",
                "timestamp" to com.google.firebase.firestore.FieldValue.serverTimestamp()
            )

            db.collection("insurance_requests")
                .add(purchaseData)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "ส่งคำขอซื้อประกันสำเร็จ", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "เกิดข้อผิดพลาด", Toast.LENGTH_SHORT).show()
                }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
