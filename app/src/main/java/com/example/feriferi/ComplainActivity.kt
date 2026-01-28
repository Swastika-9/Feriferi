package com.example.feriferi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.feriferi.ui.theme.FeriferiTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class ComplainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
               ComplainActivityScreen()
        }
    }
}



@Composable
fun ComplainActivityScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        SummaryCard("Total Reports", "5", Color.White)

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SummaryCard("Pending", "2", Color(0xFFFFFFC4))
            SummaryCard("In Progress", "2", Color(0xFFB3E5FC))
        }

        Spacer(Modifier.height(16.dp))

        SummaryCard("Resolved", "1", Color(0xFFC8E6C9))
    }
}

@Composable
fun SummaryCard(title: String, count: String, bgColor: Color) {
    Box(
        modifier = Modifier
            .width(160.dp)
            .background(bgColor, RoundedCornerShape(8.dp))
            .padding(14.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, fontWeight = FontWeight.Bold)
            Text(count, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}



@Preview
@Composable
fun ComplainActivityReport() {
    ComplainActivityScreen()
}


