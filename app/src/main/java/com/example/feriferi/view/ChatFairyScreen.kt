package com.example.feriferi.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feriferi.model.MessageModel
import com.example.feriferi.viewmodel.ChatFairyViewModel
import com.example.feriferi.ui.theme.FeriferiTheme
import androidx.compose.foundation.clickable

val PheriBackground = Color(0xFF5C637A)
val PheriHeaderBg = Color(0xFFF3E9DC)
val PheriBotHeader = Color(0xFF535865)
val PheriBubble = Color(0xFF2D264D)
val PheriYellow = Color(0xFFFFF100)

@Composable
fun ChatFairyScreen(
    viewModel: ChatFairyViewModel? = null,
    userId: String = "",
    onBackClick: () -> Unit = {}
) {
    val messages by viewModel?.messages?.collectAsState() ?: remember { mutableStateOf(emptyList<MessageModel>()) }
    var textState by remember { mutableStateOf("") }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(PheriBackground)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PheriHeaderBg)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(Icons.Default.Menu, contentDescription = null, tint = Color.Gray)
                Text(
                    text = "फेरिPheri",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF7B3F00)
                )
                Icon(Icons.Default.NotificationsNone, contentDescription = null, tint = Color.Gray)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PheriBotHeader)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("ChatFairy", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(10.dp).background(Color.Green, CircleShape))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Online Now", color = Color.Black, fontSize = 14.sp)
                    }
                }
                Icon(
                    Icons.Default.Close,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable { onBackClick() }
                )
            }

            LazyColumn(
                modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(top = 20.dp, bottom = 20.dp)
            ) {
                items(messages) { message ->
                    PheriBubbleItem(message)
                }
            }

            Card(
                modifier = Modifier.padding(20.dp).fillMaxWidth(),
                shape = RoundedCornerShape(35.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicTextField(
                        value = textState,
                        onValueChange = { textState = it },
                        modifier = Modifier.weight(1f),
                        decorationBox = { innerTextField ->
                            if (textState.isEmpty()) Text("Type a message", color = Color.Gray)
                            innerTextField()
                        }
                    )
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_share),
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    IconButton(onClick = {
                        if (textState.isNotBlank()) {
                            viewModel?.handleSendMessage(userId, textState, null)
                            textState = ""
                        }
                    }) {
                        Icon(Icons.Default.Send, contentDescription = null, tint = PheriBackground)
                    }
                }
            }
        }
    }
}

@Composable
fun PheriBubbleItem(message: MessageModel) {
    val isBot = message.isFromBot
    val align = if (isBot) Alignment.CenterStart else Alignment.CenterEnd
    val shape = if (isBot) {
        RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomEnd = 20.dp, bottomStart = 4.dp)
    } else {
        RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 20.dp, bottomEnd = 4.dp)
    }

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = align) {
        Surface(color = PheriBubble, shape = shape, modifier = Modifier.widthIn(max = 280.dp)) {
            Text(
                text = message.text,
                color = PheriYellow,
                modifier = Modifier.padding(14.dp),
                fontSize = 15.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatFairyPreview() {
    FeriferiTheme {
        ChatFairyScreen(userId = "dummy_user")
    }
}