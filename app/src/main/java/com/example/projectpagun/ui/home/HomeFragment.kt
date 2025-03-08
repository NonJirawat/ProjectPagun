package com.example.projectpagun.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projectpagun.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.projectpagun.R


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        loadInsurancePlan()

        // ใช้ Navigation Component ในการเปลี่ยนหน้า
        binding.btnSelectPlan.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_selectPlan)
        }

        binding.btnClaim.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_claim)
        }

        return binding.root
    }

    private fun loadInsurancePlan() {
        user?.let { u ->
            db.collection("insurance_plans").document(u.uid)
                .addSnapshotListener { document, error ->
                    if (error != null || document == null || !document.exists()) {
                        binding.insuranceCard.visibility = View.GONE
                        return@addSnapshotListener
                    }

                    binding.insuranceCard.visibility = View.VISIBLE
                    binding.tvInsuranceTitle.text = document.getString("title") ?: "ไม่พบข้อมูล"
                    binding.tvStartDate.text = document.getString("start_date") ?: "-"
                    binding.tvEndDate.text = document.getString("end_date") ?: "-"
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
