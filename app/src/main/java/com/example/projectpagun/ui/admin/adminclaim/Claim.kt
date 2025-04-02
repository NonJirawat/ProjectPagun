package com.example.projectpagun.ui.claim

data class Claim(
    var id: String = "",
    var registration: String = "",
    var claimDescription: String = "",
    var claimDate: String = "",
    var status: String = "pending"
)
