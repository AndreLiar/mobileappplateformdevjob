package com.ynov.jobboard

import com.google.firebase.Timestamp

data class Job(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val platform: String = "",
    val technologies: String = "",
    val modules: String? = null,
    val location: String = "",
    val contractType: String = "",
    val experienceLevel: String = "",
    val recruiterId: String = "",
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
)


