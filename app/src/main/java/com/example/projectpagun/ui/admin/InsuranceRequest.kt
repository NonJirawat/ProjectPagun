data class InsuranceRequest(
    val id: String = "",
    val uid: String = "",  // << เพิ่ม userId ที่ยื่นคำขอ
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
