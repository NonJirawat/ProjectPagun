package com.example.projectpagun.ui.insurance

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.projectpagun.databinding.ItemInsurancePlanBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PlanAdapter(private val plans: List<InsurancePlan>) :
    RecyclerView.Adapter<PlanAdapter.PlanViewHolder>() {

    inner class PlanViewHolder(val binding: ItemInsurancePlanBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val binding = ItemInsurancePlanBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PlanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        val plan = plans[position]
        holder.binding.tvTitle.text = "${plan.brand} ${plan.model} (${plan.year}) - ${plan.type}"
        holder.binding.tvDetail.text = plan.detail
        holder.binding.tvPrice.text = "ราคา ${plan.price} บาท"

        // 👇 ปุ่มซื้อ
        holder.binding.btnBuy.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user == null) {
                Toast.makeText(holder.itemView.context, "กรุณาเข้าสู่ระบบ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = hashMapOf(
                "uid" to user.uid,
                "email" to user.email,
                "plan_id" to plan.id,
                "type" to plan.type,
                "brand" to plan.brand,
                "model" to plan.model,
                "year" to plan.year,
                "price" to plan.price,
                "detail" to plan.detail,
                "status" to "pending",
                "timestamp" to com.google.firebase.firestore.FieldValue.serverTimestamp()
            )

            FirebaseFirestore.getInstance().collection("insurance_requests")
                .add(request)
                .addOnSuccessListener {
                    Toast.makeText(holder.itemView.context, "ส่งคำขอซื้อประกันสำเร็จ", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(holder.itemView.context, "เกิดข้อผิดพลาดในการซื้อ", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun getItemCount(): Int = plans.size
}