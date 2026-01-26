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
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class PaymentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PaymentActivity()
        }
    }
}


// Color constants based on your image
val BgBeige = Color(0xFFFDE8C9)
val CardPink = Color(0xFFF9A8D4)
val ButtonBrown = Color(0xFF9E5C2C)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun paymentactivity() {
    var selectedOption by remember { mutableStateOf("Credit card") }
    val options = listOf("Cash on Delivery", "Credit card", "e-sewa", "Khalti")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // --- Custom Top Bar ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("☰", fontSize = 24.sp)
            Text("फेरिPheri", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6D4C41))
            Icon(Icons.Default.Notifications, contentDescription = null, modifier = Modifier.size(24.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- Title ---
        Text(
            text = "Payment Method",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // --- Payment Options List ---
        options.forEach { text ->
            PaymentMethodItem(
                text = text,
                selected = (text == selectedOption),
                onSelect = { selectedOption = text }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.weight(1f))

        // --- Pricing Summary ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardPink, RoundedCornerShape(4.dp))
                .padding(16.dp)
        ) {
            PriceLine(label = "Transfer Amount", amount = "1043.25", hasIcon = true)
            PriceLine(label = "Additional Cost", amount = "1.75")
            Spacer(modifier = Modifier.height(8.dp))
            Box(Modifier.fillMaxWidth().height(1.dp).background(Color.Gray))
            Spacer(modifier = Modifier.height(8.dp))
            PriceLine(label = "Total", amount = "1045", isTotal = true)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Pay Button ---
        Button(
            onClick = { /* Action */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ButtonBrown),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text("Pay", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun PaymentMethodItem(text: String, selected: Boolean, onSelect: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardPink, RoundedCornerShape(4.dp))
            .selectable(selected = selected, onClick = onSelect)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val imageRes = when (text) {
            "Cash on Delivery" -> R.drawable.cashondeliver
            "Credit card" -> R.drawable.creditcard
            "e-sewa" -> R.drawable.eshewa
            "Khalti" -> R.drawable.khalti
            else -> R.drawable.google
        }
        // Icon Placeholder (White Box)
        androidx.compose.foundation.Image(
            painter = androidx.compose.ui.res.painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp, 40.dp)
                .background(Color.White)
                .border(0.5.dp, Color.Gray),
            contentScale = androidx.compose.ui.layout.ContentScale.Fit
        )

        Text(
            text = text,
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        RadioButton(
            selected = selected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(selectedColor = Color.Black)
        )
    }
}

@Composable
fun PriceLine(label: String, amount: String, hasIcon: Boolean = false, isTotal: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = if (isTotal) 20.sp else 18.sp,
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Medium
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (hasIcon) {
                Text("₹ ", fontSize = 20.sp) // Rupee symbol
            }
            Text(
                text = amount,
                fontSize = if (isTotal) 20.sp else 18.sp,
                fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}


@Preview
@Composable
fun Previewpayment() {
    paymentactivity()
}



