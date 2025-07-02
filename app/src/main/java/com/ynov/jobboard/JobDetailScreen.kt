package com.ynov.jobboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
@Composable
fun JobDetailScreen(jobId: String?, viewModel: JobViewModel) {
    val job = viewModel.jobs.collectAsState().value.find { it.id == jobId }

    if (job == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Job not found or loading...")
        }
        return
    }

    Column(modifier = Modifier.padding(24.dp)) {
        Text(job.title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text("🕒 Posted: ${job.createdAt?.toDate()?.format() ?: "Unknown"}", fontSize = 12.sp)
        Spacer(Modifier.height(16.dp))
        InfoRow("🧩 Platform", job.platform)
        InfoRow("📍 Location", job.location)
        InfoRow("💼 Contract", job.contractType)
        InfoRow("🎓 Level", job.experienceLevel)
        Spacer(Modifier.height(16.dp))
        Text("🧠 Technologies:", fontWeight = FontWeight.Medium)
        Text(job.technologies)
        job.modules?.let {
            Spacer(Modifier.height(8.dp))
            Text("📚 Modules:", fontWeight = FontWeight.Medium)
            Text(it)
        }
        Spacer(Modifier.height(16.dp))
        Text("📄 Description", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(job.description)
    }
}

