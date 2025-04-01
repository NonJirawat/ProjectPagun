package com.example.projectpagun.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.projectpagun.databinding.FragmentStatusBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class StatusFragment : Fragment() {

    private lateinit var binding: FragmentStatusBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var currentUserUid: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // ใช้ ViewBinding เพื่อเชื่อมโยงกับ UI
        binding = FragmentStatusBinding.inflate(inflater, container, false)

        db = FirebaseFirestore.getInstance()

        // ดึง UID ของผู้ใช้ปัจจุบัน
        currentUserUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        // ฟังการเปลี่ยนแปลงสถานะคำขอจาก Firestore
        db.collection("insurance_requests")
            .whereEqualTo("uid", currentUserUid)
            .addSnapshotListener { documents, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                if (documents != null && !documents.isEmpty) {
                    val request = documents.first()
                    val status = request.getString("status")
                    binding.tvStatus.text = "สถานะคำขอ: $status"
                } else {
                    binding.tvStatus.text = "ยังไม่มีคำขอ"
                }
            }

        return binding.root
    }
}
