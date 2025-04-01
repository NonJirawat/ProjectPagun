package com.example.projectpagun.ui.status

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectpagun.databinding.FragmentStatusBinding
import com.example.projectpagun.ui.admin.InsuranceRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class StatusFragment : Fragment() {

    private lateinit var binding: FragmentStatusBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var currentUserUid: String
    private lateinit var statusAdapter: StatusAdapter
    private val requestList = mutableListOf<InsuranceRequest>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatusBinding.inflate(inflater, container, false)

        db = FirebaseFirestore.getInstance()
        currentUserUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        statusAdapter = StatusAdapter(requestList)
        binding.rvStatus.layoutManager = LinearLayoutManager(context)
        binding.rvStatus.adapter = statusAdapter

        db.collection("insurance_requests")
            .whereEqualTo("userId", currentUserUid)
            .addSnapshotListener { documents, e ->
                if (e != null) return@addSnapshotListener

                requestList.clear()
                if (documents != null && !documents.isEmpty) {
                    for (doc in documents) {
                        val request = doc.toObject(InsuranceRequest::class.java)
                        requestList.add(request)
                    }
                } else {
                    requestList.add(InsuranceRequest(status = "ยังไม่มีคำขอ"))
                }
                statusAdapter.notifyDataSetChanged()
            }

        return binding.root
    }
}
