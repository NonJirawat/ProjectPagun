    package com.example.projectpagun.ui.profile

    import android.content.Intent
    import android.os.Bundle
    import android.util.Log
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.Toast
    import androidx.fragment.app.Fragment
    import com.example.projectpagun.databinding.FragmentProfileBinding
    import com.example.projectpagun.LoginActivity
    import com.google.firebase.auth.FirebaseAuth
    import com.google.firebase.firestore.FirebaseFirestore

    class ProfileFragment : Fragment() {

        private var _binding: FragmentProfileBinding? = null
        private val binding get() = _binding!!
        private val db = FirebaseFirestore.getInstance()
        private val user = FirebaseAuth.getInstance().currentUser

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
        ): View {
            _binding = FragmentProfileBinding.inflate(inflater, container, false)

            loadUserProfile()

            // ปุ่ม Logout
            binding.btnLogout.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }

            // ปุ่มแก้ไขข้อมูล
            binding.btnEditProfile.setOnClickListener {
                Toast.makeText(requireContext(), "🛠️ ยังไม่ได้เพิ่มฟีเจอร์แก้ไขข้อมูล", Toast.LENGTH_SHORT).show()
            }

            return binding.root
        }

        private fun loadUserProfile() {
            user?.let { u ->
                db.collection("insurance_requests")
                    .whereEqualTo("userId", u.uid)  // ใช้ userId เพื่อดึงข้อมูลจาก Firestore
                    .get()
                    .addOnSuccessListener { documents ->
                        if (!documents.isEmpty) {
                            val document = documents.first()
                            // แสดงข้อมูลที่ต้องการ
                            binding.tvEmail.text = "อีเมล: ${u.email ?: "-"}"
                            binding.tvCarBrand.text = "ทะเบียนรถ: ${document.getString("licensePlate") ?: "-"}"
                            binding.tvCarModel.text = "เจ้าของ: ${document.getString("ownerName") ?: "-"}"
                            binding.tvPhone.text = "เบอร์โทร: ${document.getString("phone") ?: "-"}"
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firestore", "❌ โหลดข้อมูลล้มเหลว", e)
                    }
            }
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }

