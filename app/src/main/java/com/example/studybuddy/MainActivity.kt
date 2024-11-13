package com.example.studybuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudyTimerApp()
        }
    }
}

@Composable
fun StudyTimerApp() {
    var currentScreen by remember { mutableStateOf("Login") }
    var username by remember { mutableStateOf("") }
    var timeLeft by remember { mutableStateOf(10 * 60) } // Default 10 minutes

    when (currentScreen) {
        "Login" -> LoginScreen(onLogin = { currentScreen = "Timer" })
        "Timer" -> TimerScreen(
            username,
            timeLeft,
            onSetTime = { currentScreen = "SetTime" },
            onConfirm = { currentScreen = "Confirmation" })
        "SetTime" -> SetTimeScreen(onConfirmTime = { time -> timeLeft = time; currentScreen = "Timer" })
        "Confirmation" -> ConfirmationScreen(onRestart = { currentScreen = "Timer" })
    }
}

@Composable
fun LoginScreen(onLogin: () -> Unit) {
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB2EBF2))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("STUDY TIMER", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00796B))

        Spacer(modifier = Modifier.height(32.dp))

        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onLogin,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B)),
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Login", color = Color.White)
        }
    }
}

@Composable
fun TimerScreen(username: String, timeLeft: Int, onSetTime: () -> Unit, onConfirm: () -> Unit) {
    var isRunning by remember { mutableStateOf(false) }
    var timeRemaining by remember { mutableStateOf(timeLeft) }
    var message by remember { mutableStateOf("Hello $username! Ready to study?") }

    LaunchedEffect(isRunning) {
        while (isRunning && timeRemaining > 0) {
            delay(1000L)
            timeRemaining--
            if (timeRemaining == 0) {
                message = "Time's up! Well done!"
                isRunning = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE1F5FE))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF00796B)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = String.format("%02d:%02d", timeRemaining / 60, timeRemaining % 60),
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF01579B)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { isRunning = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Start", color = Color.White)
            }
            Button(
                onClick = { isRunning = false },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Stop", color = Color.White)
            }
            Button(
                onClick = onSetTime,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B)),
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Set Time", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onConfirm,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107))
        ) {
            Text("Confirm", color = Color.White)
        }
    }
}

@Composable
fun SetTimeScreen(onConfirmTime: (Int) -> Unit) {
    var time by remember { mutableStateOf(10) } // default to 10 minutes

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB2EBF2))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Set Study Time", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00796B))

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { if (time > 1) time-- },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                modifier = Modifier.padding(8.dp)
            ) {
                Text("-", fontSize = 24.sp, color = Color.White)
            }
            Text(
                text = "$time min",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00796B)
            )
            Button(
                onClick = { time++ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                modifier = Modifier.padding(8.dp)
            ) {
                Text("+", fontSize = 24.sp, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onConfirmTime(time * 60) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B))
        ) {
            Text("Confirm", color = Color.White)
        }
    }
}

@Composable
fun ConfirmationScreen(onRestart: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB2EBF2))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Good Job!", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00796B))

        Spacer(modifier = Modifier.height(16.dp))

        Text("You completed your study session!", fontSize = 18.sp, color = Color(0xFF00796B))

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onRestart,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
            Text("Start Again", color = Color.White)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StudyTimerAppPreview() {
    StudyTimerApp()
}