package com.example.projectpagun.ui.insurance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projectpagun.R
import com.example.projectpagun.databinding.FragmentSelectPlanBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class SelectPlanFragment : Fragment() {

    private var _binding: FragmentSelectPlanBinding? = null
    private val binding get() = _binding!!

    private var selectedType = "ชั้น 1"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectPlanBinding.inflate(inflater, container, false)

        setupToolbar()
        setupDropdowns()
        setupRadioGroup()
        setupSearchButton()

        return binding.root
    }

    private fun setupToolbar() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.topAppBar) { v, insets ->
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            v.setPadding(0, statusBarHeight, 0, 0)
            insets
        }

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupDropdowns() {
        val carData = mapOf(
            "Toyota" to listOf("เลือกรุ่นรถยนต์", "Camry", "Corolla", "Vios"),
            "Honda" to listOf("เลือกรุ่นรถยนต์", "Civic", "Accord", "Jazz"),
            "Mazda" to listOf("เลือกรุ่นรถยนต์", "Mazda 3", "CX-5", "BT-50"),
            "Ford" to listOf("เลือกรุ่นรถยนต์", "Ranger", "Everest", "Mustang"),
            "Nissan" to listOf("เลือกรุ่นรถยนต์", "Almera", "Navara", "Teana")
        )

        // ✅ แก้ตรงนี้: ให้ list เป็น MutableList เพื่อใช้กับ .clear() ได้
        val brands = mutableListOf("เลือกยี่ห้อรถยนต์").apply { addAll(carData.keys) }
        val defaultModelList = mutableListOf("เลือกรุ่นรถยนต์")
        val years = mutableListOf("เลือกปีรถยนต์").apply { addAll((2010..2025).map { it.toString() }) }

        val brandAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, brands)
        val modelAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, defaultModelList)
        val yearAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, years)

        binding.spinnerBrand.adapter = brandAdapter
        binding.spinnerModel.adapter = modelAdapter
        binding.spinnerYear.adapter = yearAdapter

        binding.spinnerBrand.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
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
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupRadioGroup() {
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            selectedType = when (checkedId) {
                R.id.radioType1 -> "ชั้น 1"
                R.id.radioType2 -> "ชั้น 2"
                R.id.radioType3 -> "ชั้น 3"
                else -> "ชั้น 1"
            }
        }
    }

    private fun setupSearchButton() {
        binding.btnSearch.setOnClickListener {
            val brand = binding.spinnerBrand.selectedItem?.toString() ?: ""
            val model = binding.spinnerModel.selectedItem?.toString() ?: ""
            val year = binding.spinnerYear.selectedItem?.toString() ?: ""

            if (brand.startsWith("เลือก") || model.startsWith("เลือก") || year.startsWith("เลือก")) {
                Toast.makeText(requireContext(), "กรุณาเลือกข้อมูลให้ครบถ้วน", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val bundle = Bundle().apply {
                putString("insuranceType", selectedType)
                putString("brand", brand)
                putString("model", model)
                putString("year", year)
            }

            findNavController().navigate(R.id.searchInsuranceFragment, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
