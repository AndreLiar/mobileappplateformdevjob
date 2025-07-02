package com.ynov.jobboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun JobsScreen(viewModel: JobViewModel, navController: NavController) {
    val jobs by viewModel.jobs.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedLocation by remember { mutableStateOf("Tous") }
    var selectedContract by remember { mutableStateOf("Tous") }
    var selectedExperience by remember { mutableStateOf("Tous") }

    LaunchedEffect(Unit) {
        viewModel.fetchJobs()
    }

    val locations = listOf("Tous") + jobs.map { it.location }.distinct().sorted()
    val contracts = listOf("Tous") + jobs.map { it.contractType }.distinct().sorted()
    val experiences = listOf("Tous") + jobs.map { it.experienceLevel }.distinct().sorted()

    val filteredJobs = jobs.filter {
        (it.title.contains(searchQuery, ignoreCase = true) || it.technologies.contains(searchQuery, ignoreCase = true)) &&
                (selectedLocation == "Tous" || it.location == selectedLocation) &&
                (selectedContract == "Tous" || it.contractType == selectedContract) &&
                (selectedExperience == "Tous" || it.experienceLevel == selectedExperience)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Text(
            "📋 ERP/CRM Job Listings",
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("🔍 Rechercher un job...") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterDropdown("📍 Lieu", selectedLocation, locations) { selectedLocation = it }
            FilterDropdown("💼 Contrat", selectedContract, contracts) { selectedContract = it }
            FilterDropdown("🎓 Niveau", selectedExperience, experiences) { selectedExperience = it }
        }

        Spacer(modifier = Modifier.height(16.dp))
        TextButton(
            onClick = { navController.navigate("feedback") },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("🗣️ Donner mon avis")
        }

        Spacer(modifier = Modifier.height(16.dp))
        if (filteredJobs.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Aucun résultat trouvé")
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(filteredJobs) { job ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate("jobDetail/${job.id}")
                            },
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA))
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = job.title,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1D4ED8)
                            )
                            Text(
                                text = "🕒 Posté : ${job.createdAt?.toDate()?.format() ?: "Inconnu"}",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            InfoRow("🧩 Plateforme", job.platform)
                            InfoRow("📍 Lieu", job.location)
                            InfoRow("💼 Contrat", job.contractType)
                            InfoRow("🎓 Niveau", job.experienceLevel)

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = job.description.take(160) + "...",
                                fontSize = 14.sp,
                                maxLines = 3
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            if (job.technologies.isNotBlank()) {
                                Text("🧠 TECH STACK:", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                Text(job.technologies, color = Color(0xFF3B82F6))
                                Spacer(modifier = Modifier.height(6.dp))
                            }

                            if (!job.modules.isNullOrBlank()) {
                                Text("📚 MODULES:", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                Text(job.modules!!, color = Color(0xFF16A34A))
                                Spacer(modifier = Modifier.height(6.dp))
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = {
                                    navController.navigate("jobDetail/${job.id}")
                                },
                                modifier = Modifier.align(Alignment.End),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF6366F1),
                                    contentColor = Color.White
                                )
                            ) {
                                Text("🚀 Postuler")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilterDropdown(
    label: String,
    selected: String,
    options: List<String>,
    onSelectedChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        OutlinedButton(onClick = { expanded = true }) {
            Text("$label: $selected")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelectedChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(label, fontWeight = FontWeight.Medium, fontSize = 13.sp)
        Text(value, fontSize = 13.sp)
    }
}

fun Date.format(): String {
    val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return sdf.format(this)
}
