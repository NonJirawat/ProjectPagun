package com.example.projectpagun.ui.insurance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
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

        setupDropdowns()
        setupInsuranceTypeSelection()

        binding.btnSearch.setOnClickListener {
            val brand = binding.spinnerBrand.selectedItem.toString()
            val model = binding.spinnerModel.selectedItem.toString()
            val year = binding.spinnerYear.selectedItem.toString()

            // ✅ ส่งข้อมูลไปยัง SearchInsuranceFragment ด้วย Bundle แทน SafeArgs
            val bundle = Bundle().apply {
                putString("selectedType", selectedType)
                putString("brand", brand)
                putString("model", model)
                putString("year", year)
            }
            findNavController().navigate(R.id.searchInsuranceFragment, bundle)
        }

        return binding.root
    }

    private fun setupDropdowns() {
        val brands = arrayOf("Toyota", "Honda", "Mazda", "Nissan", "Ford")
        val models = arrayOf("Camry", "Civic", "CX-5", "Almera", "Ranger")
        val years = (2000..2025).map { it.toString() }.toTypedArray()

        binding.spinnerBrand.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, brands)
        binding.spinnerModel.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, models)
        binding.spinnerYear.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, years)
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
