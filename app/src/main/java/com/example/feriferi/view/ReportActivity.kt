package com.example.feriferi.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


class ReportActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
                ReportDetailScreen()
            }
        }
    }


@Composable
fun ReportDetailScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        LabelValue("Report Type", "Vendor Behavior")

        Spacer(modifier = Modifier.height(12.dp))

        SectionCard("Reporter Information") {
            Text("Hari Prasad", fontWeight = FontWeight.Medium)
            Text("+977 98XXXXXXXX", color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(12.dp))

        LabelValue("Vendor", "Daily Needs Store")
        LabelValue("Product", "N/A")

        Spacer(modifier = Modifier.height(12.dp))

        SectionCard("Complaint Details") {
            Text(
                "Vendor was rude and unprofessional during delivery. Very poor customer service.",
                color = Color.DarkGray
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        ActionButton(
            text = "Mark as In Progress",
            color = Color(0xFF2563EB)
        ) {
            Toast.makeText(context, "Marked In Progress", Toast.LENGTH_SHORT).show()
        }

        Spacer(modifier = Modifier.height(12.dp))

        ActionButton(
            text = "Resolve Report",
            color = Color(0xFF16A34A)
        ) {
            Toast.makeText(context, "Report Resolved", Toast.LENGTH_SHORT).show()
        }

        Spacer(modifier = Modifier.height(12.dp))

        ActionButton(
            text = "Contact Reporter",
            color = Color.LightGray,
            textColor = Color.Black
        ) {
            Toast.makeText(context, "Contact Reporter clicked", Toast.LENGTH_SHORT).show()
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    Toast.makeText(context, "Report Deleted", Toast.LENGTH_SHORT).show()
                },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Delete Report",
                color = Color.Red,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun LabelValue(label: String, value: String) {
    Column {
        Text(label, fontSize = 14.sp, color = Color.Gray)
        Text(value, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun SectionCard(title: String, content: @Composable () -> Unit) {
    Column {
        Text(title, fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(6.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F4F6))
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                content()
            }
        }
    }
}

@Composable
fun ActionButton(
    text: String,
    color: Color,
    textColor: Color = Color.White,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) {
        Text(text, fontSize = 16.sp, color = textColor)
    }
}


@Preview
@Composable
fun PreviewReport() {
    ReportDetailScreen()
}



