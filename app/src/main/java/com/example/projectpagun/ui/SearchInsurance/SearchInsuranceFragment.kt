package com.example.projectpagun.ui.insurance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectpagun.databinding.FragmentSearchInsuranceBinding
import com.google.firebase.firestore.FirebaseFirestore

data class InsurancePlan(
    val id: String = "",
    val type: String = "",
    val brand: String = "",
    val model: String = "",
    val year: String = "",
    val price: Long = 0,
    val detail: String = "",
    val insuranceAmount: Long = 0,  // ทุนประกัน
    val excessAmount: Long = 0  // ค่าเสียหายส่วนแรก
)
class SearchInsuranceFragment : Fragment() {

    private var _binding: FragmentSearchInsuranceBinding? = null
    private val binding get() = _binding!!
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchInsuranceBinding.inflate(inflater, container, false)

        val insuranceType = arguments?.getString("insuranceType") ?: ""
        val brand = arguments?.getString("brand") ?: ""
        val model = arguments?.getString("model") ?: ""
        val year = arguments?.getString("year") ?: ""

        binding.recyclerPlans.layoutManager = LinearLayoutManager(requireContext())

        firestore.collection("insurance_plans")
            .whereEqualTo("type", insuranceType)
            .whereEqualTo("brand", brand)
            .whereEqualTo("model", model)
            .whereEqualTo("year", year)
            .get()
            .addOnSuccessListener { result ->
                val plans = result.map {
                    InsurancePlan(
                        id = it.id,
                        type = it.getString("type") ?: "",
                        brand = it.getString("brand") ?: "",
                        model = it.getString("model") ?: "",
                        year = it.getString("year") ?: "",
                        price = it.getLong("price") ?: 0,
                        detail = it.getString("detail") ?: "",
                        insuranceAmount = it.getLong("insuranceAmount") ?: 0,  // ทุนประกัน
                        excessAmount = it.getLong("excessAmount") ?: 0  // ค่าเสียหายส่วนแรก
                    )
                }

                val adapter = PlanAdapter(plans)
                binding.recyclerPlans.adapter = adapter
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "เกิดข้อผิดพลาดในการโหลดแผนประกัน", Toast.LENGTH_SHORT).show()
            }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
