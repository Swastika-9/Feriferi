package com.example.feriferi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feriferi.model.ReportActivity
import com.example.feriferi.repository.ReportRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReportViewModel(private val repository: ReportRepository) : ViewModel() {

    private val _reportState = MutableStateFlow<ReportActivity.Report?>(null)
    val reportState: StateFlow<ReportActivity.Report?> = _reportState

    fun loadReport(reportId: String) {
        viewModelScope.launch {
            val report = repository.getReport(reportId)
            _reportState.value = report
        }
    }

    fun markInProgress(reportId: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = repository.updateStatus(reportId, "InProgress")
            onResult(success)
            if (success) loadReport(reportId)
        }
    }

    fun resolveReport(reportId: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = repository.updateStatus(reportId, "Resolved")
            onResult(success)
            if (success) loadReport(reportId)
        }
    }

    fun deleteReport(reportId: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = repository.deleteReport(reportId)
            onResult(success)
        }
    }
}
