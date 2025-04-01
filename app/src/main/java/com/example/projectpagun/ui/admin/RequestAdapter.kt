package com.example.projectpagun.ui.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.projectpagun.databinding.ItemInsuranceRequestBinding
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

// ข้อมูลคำขอซื้อประกัน
data class InsuranceRequest(
    val id: String = "",
    val uid: String = "", // เพิ่ม uid เพื่อเชื่อมโยงกับผู้ใช้
    val ownerName: String = "",
    val phone: String = "",
    val licensePlate: String = "",
    val type: String = "",
    val brand: String = "",
    val model: String = "",
    val year: String = "",
    val price: Long = 0,
    var status: String = "pending"
)

class RequestAdapter(private val requests: List<InsuranceRequest>) :
    RecyclerView.Adapter<RequestAdapter.RequestViewHolder>() {

    private val db = FirebaseFirestore.getInstance()

    inner class RequestViewHolder(val binding: ItemInsuranceRequestBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val binding = ItemInsuranceRequestBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return RequestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val request = requests[position]

        // ตั้งค่าข้อมูล
        holder.binding.tvOwnerName.text = "ชื่อ: ${request.ownerName.ifEmpty { "-" }}"
        holder.binding.tvPhone.text = "เบอร์โทร: ${request.phone.ifEmpty { "-" }}"
        holder.binding.tvLicense.text = "ทะเบียนรถ: ${request.licensePlate.ifEmpty { "-" }}"
        holder.binding.tvPlanInfo.text = "แผน: ${request.type} - ${request.brand} ${request.model} ${request.year}"
        holder.binding.tvStatus.text = "สถานะ: ${request.status}"

        // สีสถานะ
        holder.binding.tvStatus.setTextColor(
            when (request.status) {
                "approved" -> 0xFF4CAF50.toInt() // เขียว
                "rejected" -> 0xFFF44336.toInt() // แดง
                else -> 0xFFFF9800.toInt()       // เหลือง
            }
        )

        // ซ่อนปุ่มถ้าไม่ใช่ pending
        if (request.status != "pending") {
            holder.binding.btnApprove.visibility = View.GONE
            holder.binding.btnReject.visibility = View.GONE
        } else {
            holder.binding.btnApprove.visibility = View.VISIBLE
            holder.binding.btnReject.visibility = View.VISIBLE
        }

        // ปุ่ม "อนุมัติ"
        holder.binding.btnApprove.setOnClickListener {
            updateStatus(request, "approved", holder)
        }

        // ปุ่ม "ปฏิเสธ"
        holder.binding.btnReject.setOnClickListener {
            updateStatus(request, "rejected", holder)
        }
    }

    private fun updateStatus(request: InsuranceRequest, newStatus: String, holder: RequestViewHolder) {
        val updateData = mutableMapOf<String, Any>(
            "status" to newStatus
        )

        db.collection("insurance_requests").document(request.id)
            .update(updateData)
            .addOnSuccessListener {
                request.status = newStatus
                notifyItemChanged(holder.adapterPosition)
                Toast.makeText(holder.itemView.context, "อัปเดตสถานะเป็น $newStatus", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(holder.itemView.context, "อัปเดตล้มเหลว", Toast.LENGTH_SHORT).show()
            }
    }

    override fun getItemCount(): Int = requests.size
}
