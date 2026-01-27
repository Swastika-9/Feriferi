package com.example.feriferi.model

class ReportActivity {
    data class Report(
        val reporterName: String = "",
        val reporterPhone: String = "",
        val vendor: String = "",
        val product: String = "",
        val reportType: String = "",
        val complaint: String = "",
        val status: String = "Pending"
    )
}