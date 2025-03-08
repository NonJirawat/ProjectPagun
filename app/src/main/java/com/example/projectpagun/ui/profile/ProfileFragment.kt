package com.example.projectpagun.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.projectpagun.databinding.FragmentProfileBinding
import com.example.projectpagun.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // แสดงชื่ออีเมลของผู้ใช้ปัจจุบัน
        val user = FirebaseAuth.getInstance().currentUser
        binding.textProfile.text = user?.email ?: "No User Logged In"

        // ปุ่ม Logout
        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut() // ออกจากระบบ
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent) // กลับไปหน้า Login
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
