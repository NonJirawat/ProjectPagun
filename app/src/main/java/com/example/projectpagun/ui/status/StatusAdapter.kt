package com.example.projectpagun.ui.status

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectpagun.R
import com.example.projectpagun.ui.admin.InsuranceRequest
import android.graphics.Color


class StatusAdapter(private val requests: List<InsuranceRequest>) :
    RecyclerView.Adapter<StatusAdapter.StatusViewHolder>() {

    inner class StatusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvPlate: TextView = itemView.findViewById(R.id.tvPlate)
        val tvPhone: TextView = itemView.findViewById(R.id.tvPhone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_status, parent, false)
        return StatusViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        val request = requests[position]
        holder.tvStatus.text = "สถานะ: ${request.status}"
        holder.tvName.text = "เจ้าของ: ${request.ownerName}"
        holder.tvPlate.text = "ทะเบียน: ${request.licensePlate}"
        holder.tvPhone.text = "เบอร์โทร: ${request.phone}"

        // เปลี่ยนสีตามสถานะ
        when (request.status.lowercase()) {
            "approved" -> holder.tvStatus.setTextColor(Color.parseColor("#4CAF50")) // เขียว
            "pending" -> holder.tvStatus.setTextColor(Color.parseColor("#FFC107"))  // เหลือง
            "rejected" -> holder.tvStatus.setTextColor(Color.parseColor("#F44336")) // แดง
            else -> holder.tvStatus.setTextColor(Color.DKGRAY)
        }
    }


    override fun getItemCount(): Int = requests.size
}
