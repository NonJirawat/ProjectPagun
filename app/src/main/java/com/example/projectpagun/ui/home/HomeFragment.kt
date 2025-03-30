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
import java.text.SimpleDateFormat
import java.util.*

import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

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

        binding.btnSelectPlan.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_selectPlan)
        }

        binding.btnClaim.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_claim)
        }

        return binding.root
    }

    // ✅ จัด status bar padding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(binding.topAppBar) { v, insets ->
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            v.setPadding(0, statusBarHeight, 0, 0) // 👈 เพิ่ม padding top เท่าความสูง status bar
            insets
        }
    }

    private fun loadInsurancePlan() {
        user?.let { u ->
            db.collection("insurance_plans").document(u.uid)
                .addSnapshotListener { document, error ->
                    if (error != null || document == null || !document.exists()) {
                        binding.insuranceCard.visibility = View.GONE
                        return@addSnapshotListener
                    }

                    val startDateTimestamp = document.getTimestamp("start_date")
                    val endDateTimestamp = document.getTimestamp("end_date")

                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val startDateString = startDateTimestamp?.toDate()?.let { dateFormat.format(it) } ?: "-"
                    val endDateString = endDateTimestamp?.toDate()?.let { dateFormat.format(it) } ?: "-"

                    binding.insuranceCard.visibility = View.VISIBLE
                    binding.tvInsuranceTitle.text = document.getString("title") ?: "ไม่พบข้อมูล"
                    binding.tvStartDate.text = "เริ่มต้น: $startDateString"
                    binding.tvEndDate.text = "หมดอายุ: $endDateString"
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
