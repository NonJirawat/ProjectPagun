package com.example.projectpagun.model

data class Claim(
    val userId: String = "",  // เก็บ userId ของผู้ที่ทำการเคลม
    val registration: String = "", // หมายเลขทะเบียนรถ
    val claimDescription: String = "", // รายละเอียดการเคลม
    val claimDate: String = "", // วันที่เกิดเหตุ
    val status: String = "", // สถานะการเคลม (เช่น pending, approved)
    val timestamp: Any = "" // timestamp สำหรับเวลา
)
