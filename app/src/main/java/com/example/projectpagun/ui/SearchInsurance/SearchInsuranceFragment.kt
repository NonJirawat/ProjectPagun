package com.example.projectpagun.ui.insurance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectpagun.databinding.FragmentSearchInsuranceBinding
import com.google.firebase.firestore.FirebaseFirestore

class SearchInsuranceFragment : Fragment() {

    private var _binding: FragmentSearchInsuranceBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: InsuranceAdapter
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchInsuranceBinding.inflate(inflater, container, false)

        // ✅ ใช้ Bundle แทน Safe Args
        val bundle = arguments
        val selectedType = bundle?.getString("selectedType") ?: "ชั้น 1"
        val brand = bundle?.getString("brand") ?: "Toyota"
        val model = bundle?.getString("model") ?: "Camry"
        val year = bundle?.getString("year") ?: "2023"

        setupRecyclerView()
        loadInsurancePlans(selectedType, brand, model, year)

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = InsuranceAdapter { selectedInsurance ->
            // ✅ ทำอะไรบางอย่างเมื่อกดเลือกแผนประกัน เช่น บันทึกลง Firestore
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun loadInsurancePlans(type: String, brand: String, model: String, year: String) {
        db.collection("insurance_plans")
            .whereEqualTo("type", type)
            .whereEqualTo("car_brand", brand)
            .whereEqualTo("car_model", model)
            .whereEqualTo("car_year", year)
            .get()
            .addOnSuccessListener { documents ->
                val insuranceList = documents.mapNotNull { it.toObject(InsuranceModel::class.java) }
                adapter.submitList(insuranceList)
            }
            .addOnFailureListener {
                // ✅ แสดงข้อความเมื่อโหลดข้อมูลล้มเหลว
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
