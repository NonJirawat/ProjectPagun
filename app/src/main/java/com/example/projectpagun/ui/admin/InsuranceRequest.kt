data class InsuranceRequest(
    val id: String = "",
    val uid: String = "",
    val email: String = "",
    val ownerName: String = "",
    val phone: String = "",
    val licensePlate: String = "",
    val address: String = "",
    val type: String = "",
    val brand: String = "",
    val model: String = "",
    val year: String = "",
    val price: Long = 0,
    val detail: String = "",
    var status: String = "pending"
)
