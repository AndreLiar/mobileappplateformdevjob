package com.ynov.jobboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun FeedbackScreen(navBack: () -> Unit) {
    var message by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isSubmitted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("🗣️ Donnez votre avis", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            label = { Text("Votre message") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 6
        )

        if (isSubmitted) {
            Text("✅ Merci pour votre retour !", color = MaterialTheme.colorScheme.primary)
        }

        Button(
            onClick = {
                if (message.isNotBlank()) {
                    isLoading = true
                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                    val feedback = hashMapOf(
                        "message" to message,
                        "createdAt" to Timestamp.now(),
                        "userId" to userId
                    )
                    FirebaseFirestore.getInstance()
                        .collection("feedbacks")
                        .add(feedback)
                        .addOnSuccessListener {
                            isSubmitted = true
                            message = ""
                        }
                        .addOnFailureListener {
                            // Log error or show snackbar
                        }
                        .addOnCompleteListener {
                            isLoading = false
                        }
                }
            },
            enabled = !isLoading && message.isNotBlank()
        ) {
            Text("Envoyer")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = navBack) {
            Text("⬅️ Retour")
        }
    }
}
