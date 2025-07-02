package com.ynov.jobboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class JobViewModel : ViewModel() {
    private val _jobs = MutableStateFlow<List<Job>>(emptyList())
    val jobs: StateFlow<List<Job>> = _jobs

    fun fetchJobs() {
        viewModelScope.launch {
            FirebaseFirestore.getInstance()
                .collection("jobs")
                .get()
                .addOnSuccessListener { result ->
                    val jobList = result.mapNotNull { doc ->
                        try {
                            val createdAt = (doc["createdAt"] as? Timestamp)?.toDate()?.toString() ?: ""
                            val updatedAt = (doc["updatedAt"] as? Timestamp)?.toDate()?.toString() ?: ""

                            Job(
                                id = doc.id,
                                title = doc.getString("title") ?: "",
                                description = doc.getString("description") ?: "",
                                platform = doc.getString("platform") ?: "",
                                technologies = doc.getString("technologies") ?: "",
                                modules = doc.getString("modules"),
                                location = doc.getString("location") ?: "",
                                contractType = doc.getString("contractType") ?: "",
                                experienceLevel = doc.getString("experienceLevel") ?: "",
                                recruiterId = doc.getString("recruiterId") ?: "",
                            )
                        } catch (e: Exception) {
                            null // Skip malformed job
                        }
                    }
                    _jobs.value = jobList
                }
                .addOnFailureListener {
                    it.printStackTrace()
                }
        }
    }
}
