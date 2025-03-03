package com.example.projectpagun.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.projectpagun.databinding.FragmentDashboardBinding
import com.google.firebase.firestore.FirebaseFirestore

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // กดปุ่มแล้วเพิ่มข้อมูลไป Firestore
        binding.buttonAddData.setOnClickListener {
            addDataToFirestore()
        }

        return root
    }

    private fun addDataToFirestore() {
        val data = hashMapOf(
            "title" to "Test Title",
            "description" to "This is a test description",
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("dashboard_data")
            .add(data)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "เพิ่มข้อมูลสำเร็จ!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "เกิดข้อผิดพลาด: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
