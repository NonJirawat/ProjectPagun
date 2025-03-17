package com.example.projectpagun.ui.insurance

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projectpagun.R
import com.example.projectpagun.databinding.FragmentSelectPlanBinding

class SelectPlanFragment : Fragment() {

    private var _binding: FragmentSelectPlanBinding? = null
    private val binding get() = _binding!!

    private var selectedType = "ชั้น 1" // ค่าเริ่มต้น

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectPlanBinding.inflate(inflater, container, false)

        try {
            setupDropdowns()
            setupInsuranceTypeSelection()
        } catch (e: Exception) {
            Log.e("SelectPlanFragment", "Error initializing dropdowns", e)
            Toast.makeText(requireContext(), "เกิดข้อผิดพลาดในการโหลดข้อมูล", Toast.LENGTH_SHORT).show()
        }

        binding.btnSearch.setOnClickListener {
            try {
                val brand = binding.spinnerBrand.selectedItem?.toString() ?: "เลือกยี่ห้อรถยนต์"
                val model = binding.spinnerModel.selectedItem?.toString() ?: "เลือกรุ่นรถยนต์"
                val year = binding.spinnerYear.selectedItem?.toString() ?: "เลือกปีรถยนต์"

                // ✅ ตรวจสอบก่อนนำไปใช้
                if (brand == "เลือกยี่ห้อรถยนต์" || model == "เลือกรุ่นรถยนต์" || year == "เลือกปีรถยนต์") {
                    Toast.makeText(requireContext(), "กรุณาเลือกข้อมูลให้ครบถ้วน", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val bundle = Bundle().apply {
                    putString("selectedType", selectedType)
                    putString("brand", brand)
                    putString("model", model)
                    putString("year", year)
                }
                findNavController().navigate(R.id.searchInsuranceFragment, bundle)

            } catch (e: Exception) {
                Log.e("SelectPlanFragment", "Error in btnSearch click", e)
                Toast.makeText(requireContext(), "เกิดข้อผิดพลาด กรุณาลองใหม่", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun setupDropdowns() {
        val carData = mapOf(
            "Toyota" to listOf("เลือกรุ่นรถยนต์", "Camry", "Corolla", "Vios"),
            "Honda" to listOf("เลือกรุ่นรถยนต์", "Civic", "Accord", "Jazz"),
            "Mazda" to listOf("เลือกรุ่นรถยนต์", "Mazda 3", "CX-5", "BT-50"),
            "Ford" to listOf("เลือกรุ่นรถยนต์", "Ranger", "Everest", "Mustang"),
            "Nissan" to listOf("เลือกรุ่นรถยนต์", "Almera", "Navara", "Teana")
        )

        val brands = listOf("เลือกยี่ห้อรถยนต์") + carData.keys.toList()
        val defaultModelList = listOf("เลือกรุ่นรถยนต์")
        val years = listOf("เลือกปีรถยนต์") + (2000..2025).map { it.toString() }

        val brandAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, brands)
        val modelAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, defaultModelList)
        val yearAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, years)

        binding.spinnerBrand.adapter = brandAdapter
        binding.spinnerModel.adapter = modelAdapter
        binding.spinnerYear.adapter = yearAdapter

        binding.spinnerBrand.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                try {
                    if (position == 0) {
                        modelAdapter.clear()
                        modelAdapter.addAll(defaultModelList)
                        modelAdapter.notifyDataSetChanged()
                        binding.spinnerModel.setSelection(0, false)
                        return
                    }

                    val selectedBrand = brands[position]
                    val models = carData[selectedBrand] ?: defaultModelList

                    modelAdapter.clear()
                    modelAdapter.addAll(models)
                    modelAdapter.notifyDataSetChanged()
                    binding.spinnerModel.setSelection(0, false)

                } catch (e: Exception) {
                    Log.e("SelectPlanFragment", "Error updating model spinner", e)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupInsuranceTypeSelection() {
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            selectedType = when (checkedId) {
                R.id.radioType1 -> "ชั้น 1"
                R.id.radioType2 -> "ชั้น 2"
                R.id.radioType3 -> "ชั้น 3"
                else -> "ชั้น 1"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
