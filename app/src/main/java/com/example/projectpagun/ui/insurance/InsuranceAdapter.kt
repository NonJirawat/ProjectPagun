package com.example.projectpagun.ui.insurance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectpagun.databinding.ItemInsuranceBinding

class InsuranceAdapter(private val onSelect: (InsuranceModel) -> Unit) :
    RecyclerView.Adapter<InsuranceAdapter.InsuranceViewHolder>() {

    private val items = mutableListOf<InsuranceModel>()

    fun submitList(list: List<InsuranceModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InsuranceViewHolder {
        val binding = ItemInsuranceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InsuranceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InsuranceViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class InsuranceViewHolder(private val binding: ItemInsuranceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(insurance: InsuranceModel) {
            binding.tvInsuranceTitle.text = insurance.title
            binding.tvPrice.text = "ราคา: ${insurance.price} บาท"

            binding.btnSelect.setOnClickListener {
                onSelect(insurance) // เมื่อกดเลือกแผนประกัน
            }
        }
    }
}
