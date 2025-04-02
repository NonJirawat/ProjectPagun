package com.example.projectpagun.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectpagun.databinding.ItemClaimStatusBinding
import com.example.projectpagun.model.Claim

class ClaimStatusAdapter(private val claims: List<Claim>) : RecyclerView.Adapter<ClaimStatusAdapter.ClaimViewHolder>() {

    // ViewHolder ที่เก็บการแสดงผลของรายการใน RecyclerView
    inner class ClaimViewHolder(private val binding: ItemClaimStatusBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(claim: Claim) {
            binding.tvClaimRegistration.text = claim.registration
            binding.tvClaimDescription.text = claim.claimDescription
            binding.tvClaimDate.text = claim.claimDate
            binding.tvClaimStatus.text = claim.status
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClaimViewHolder {
        val binding = ItemClaimStatusBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClaimViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClaimViewHolder, position: Int) {
        val claim = claims[position]
        holder.bind(claim)
    }

    override fun getItemCount(): Int = claims.size
}
