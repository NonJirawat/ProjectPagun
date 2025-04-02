package com.example.projectpagun.ui.claim

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectpagun.R
import android.widget.ArrayAdapter


class ClaimAdapter(
    private val claims: List<Claim>,
    private val onStatusChange: (Claim, String) -> Unit
) : RecyclerView.Adapter<ClaimAdapter.ClaimViewHolder>() {

    inner class ClaimViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvRegistration: TextView = view.findViewById(R.id.tvRegistration)
        val tvClaimDescription: TextView = view.findViewById(R.id.tvClaimDescription)
        val tvClaimDate: TextView = view.findViewById(R.id.tvClaimDate)
        val spinnerStatus: Spinner = view.findViewById(R.id.spinnerStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClaimViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_claim_admin, parent, false)
        return ClaimViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClaimViewHolder, position: Int) {
        val claim = claims[position]

        // ตั้งค่าข้อมูล
        holder.tvRegistration.text = claim.registration
        holder.tvClaimDescription.text = claim.claimDescription
        holder.tvClaimDate.text = claim.claimDate

        // ตั้งค่าค่า spinner สำหรับสถานะ
        // ตั้งค่าเริ่มต้นให้เป็นสถานะของ claim
        val statusAdapter = ArrayAdapter.createFromResource(
            holder.itemView.context,
            R.array.claim_status,
            android.R.layout.simple_spinner_item
        )
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        holder.spinnerStatus.adapter = statusAdapter

        // กำหนดค่าคะแนนสถานะ
        val statusPosition = statusAdapter.getPosition(claim.status)
        holder.spinnerStatus.setSelection(statusPosition)

        // ฟังค์ชันในการเปลี่ยนแปลงสถานะ
        holder.spinnerStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                val selectedStatus = parent.getItemAtPosition(position).toString()
                onStatusChange(claim, selectedStatus)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    override fun getItemCount(): Int = claims.size
}
