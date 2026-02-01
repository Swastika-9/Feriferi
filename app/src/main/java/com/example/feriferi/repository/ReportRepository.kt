package com.example.feriferi.repository

import com.example.feriferi.model.ReportActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ReportRepository {

    private val db = FirebaseFirestore.getInstance()

    // Fetch report by ID
    suspend fun getReport(reportId: String): ReportActivity.Report? {
        return try {
            val snapshot = db.collection("reports").document(reportId).get().await()
            snapshot.toObject(ReportActivity.Report::class.java)
        } catch (e: Exception) {
            null
        }
    }

    // Update status
    suspend fun updateStatus(reportId: String, status: String): Boolean {
        return try {
            db.collection("reports").document(reportId).update("status", status).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // Delete report
    suspend fun deleteReport(reportId: String): Boolean {
        return try {
            db.collection("reports").document(reportId).delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }
}
