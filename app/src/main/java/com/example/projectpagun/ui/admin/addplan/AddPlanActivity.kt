package com.example.projectpagun.ui.admin.addplan

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.projectpagun.R
import com.google.firebase.firestore.FirebaseFirestore
import android.view.View


class AddPlanActivity : AppCompatActivity() {

    private lateinit var spinnerType: Spinner
    private lateinit var spinnerBrand: Spinner
    private lateinit var spinnerModel: Spinner
    private lateinit var spinnerYear: Spinner
    private lateinit var etPrice: EditText
    private lateinit var etDetail: EditText
    private lateinit var etInsuranceAmount: EditText
    private lateinit var etExcessAmount: EditText
    private lateinit var btnAddPlan: Button

    private val carData = mapOf(
        "Toyota" to listOf("Camry", "Corolla", "Vios"),
        "Honda" to listOf("Civic", "Accord", "Jazz"),
        "Mazda" to listOf("Mazda 3", "CX-5", "BT-50"),
        "Ford" to listOf("Ranger", "Everest", "Mustang"),
        "Nissan" to listOf("Almera", "Navara", "Teana")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_plan)

        spinnerType = findViewById(R.id.spinnerType)
        spinnerBrand = findViewById(R.id.spinnerBrand)
        spinnerModel = findViewById(R.id.spinnerModel)
        spinnerYear = findViewById(R.id.spinnerYear)
        etPrice = findViewById(R.id.etPrice)
        etDetail = findViewById(R.id.etDetail)
        etInsuranceAmount = findViewById(R.id.etInsuranceAmount)
        etExcessAmount = findViewById(R.id.etExcessAmount)
        btnAddPlan = findViewById(R.id.btnAddPlan)

        setupDropdowns()

        btnAddPlan.setOnClickListener {
            addPlanToFirebase()
        }
    }

    private fun setupDropdowns() {
        val types = listOf("ชั้น 1", "ชั้น 2", "ชั้น 3")
        val brands = carData.keys.toList()
        val years = (2010..2025).map { it.toString() }

        spinnerType.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, types)
        spinnerBrand.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, brands)
        spinnerYear.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, years)

        // Default model list
        val defaultModelList = mutableListOf<String>()
        val modelAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, defaultModelList)
        spinnerModel.adapter = modelAdapter

        spinnerBrand.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val brand = brands[position]
                val models = carData[brand] ?: listOf()
                modelAdapter.clear()
                modelAdapter.addAll(models)
                modelAdapter.notifyDataSetChanged()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun addPlanToFirebase() {
        val type = spinnerType.selectedItem.toString()
        val brand = spinnerBrand.selectedItem.toString()
        val model = spinnerModel.selectedItem.toString()
        val year = spinnerYear.selectedItem.toString()
        val priceText = etPrice.text.toString().trim()
        val detail = etDetail.text.toString().trim()
        val insuranceAmountText = etInsuranceAmount.text.toString().trim()
        val excessAmountText = etExcessAmount.text.toString().trim()

        if (priceText.isEmpty() || model.isEmpty() || insuranceAmountText.isEmpty() || excessAmountText.isEmpty()) {
            Toast.makeText(this, "กรุณากรอกข้อมูลให้ครบถ้วน", Toast.LENGTH_SHORT).show()
            return
        }

        val price = priceText.toLongOrNull() ?: 0
        val insuranceAmount = insuranceAmountText.toLongOrNull() ?: 0
        val excessAmount = excessAmountText.toLongOrNull() ?: 0

        val plan = hashMapOf(
            "type" to type,
            "brand" to brand,
            "model" to model,
            "year" to year,
            "price" to price,
            "detail" to detail,
            "insuranceAmount" to insuranceAmount,
            "excessAmount" to excessAmount
        )

        FirebaseFirestore.getInstance().collection("insurance_plans")
            .add(plan)
            .addOnSuccessListener {
                Toast.makeText(this, "เพิ่มแผนประกันสำเร็จ", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "เกิดข้อผิดพลาด", Toast.LENGTH_SHORT).show()
            }
    }
}
