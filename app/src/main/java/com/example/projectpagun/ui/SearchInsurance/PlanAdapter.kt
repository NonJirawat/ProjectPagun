package com.example.projectpagun.ui.insurance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.projectpagun.R
import com.example.projectpagun.databinding.ItemInsurancePlanBinding

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

        // 👇 ปุ่มซื้อ -> ไปหน้า ConfirmPurchaseFragment
        holder.binding.btnBuy.setOnClickListener {
            val bundle = Bundle().apply {
                putString("planId", plan.id)
                putString("title", "${plan.brand} ${plan.model} - ${plan.type}")
                putLong("price", plan.price)
            }
            it.findNavController().navigate(R.id.confirmPurchaseFragment, bundle)
        }
    }

    override fun getItemCount(): Int = plans.size
}
