package com.example.projectpagun.ui.admin

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectpagun.databinding.ActivityClaimListBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.example.projectpagun.ui.admin.InsuranceRequest
import com.example.projectpagun.ui.admin.RequestAdapter



class ClaimListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClaimListBinding
    private val db = FirebaseFirestore.getInstance()

    private var allRequests = listOf<InsuranceRequest>()
    private lateinit var adapter: RequestAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClaimListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerRequests.layoutManager = LinearLayoutManager(this)
        adapter = RequestAdapter(listOf())
        binding.recyclerRequests.adapter = adapter

        val statuses = listOf("ทั้งหมด", "approved", "pending", "rejected")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, statuses)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerStatusFilter.adapter = spinnerAdapter

        binding.spinnerStatusFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selected = statuses[position]
                filterRequestsByStatus(selected)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        loadAllRequests()
    }

    private fun loadAllRequests() {
        db.collection("insurance_requests")
            .get()
            .addOnSuccessListener { result ->
                allRequests = result.map {
                    InsuranceRequest(
                        id = it.id,

                        ownerName = it.getString("ownerName") ?: "",
                        phone = it.getString("phone") ?: "",
                        licensePlate = it.getString("licensePlate") ?: "",
                        type = it.getString("type") ?: "",
                        brand = it.getString("brand") ?: "",
                        model = it.getString("model") ?: "",
                        year = it.getString("year") ?: "",
                        price = it.getLong("price") ?: 0,

                        status = it.getString("status") ?: "pending"
                    )
                }
                filterRequestsByStatus(binding.spinnerStatusFilter.selectedItem.toString())
            }
            .addOnFailureListener {
                Toast.makeText(this, "โหลดข้อมูลล้มเหลว", Toast.LENGTH_SHORT).show()
            }
    }

    private fun filterRequestsByStatus(status: String) {
        val filtered = if (status == "ทั้งหมด") {
            allRequests
        } else {
            allRequests.filter { it.status == status }
        }
        adapter = RequestAdapter(filtered)
        binding.recyclerRequests.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}
