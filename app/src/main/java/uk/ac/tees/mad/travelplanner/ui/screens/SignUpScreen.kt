package uk.ac.tees.mad.travelplanner.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import uk.ac.tees.mad.travelplanner.ui.app_navigation.Screen
import uk.ac.tees.mad.travelplanner.viewmodels.UiState
import uk.ac.tees.mad.travelplanner.viewmodels.AuthViewModel

@Composable
fun SignUpScreen(navController: NavHostController, viewModel: AuthViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }
    var isSignUpEnabled by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        Color.White
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Create Account",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Sign up to start planning your adventures",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(32.dp))

            TextField(
                value = name,
                onValueChange = {
                    name = it
                    isSignUpEnabled = name.isNotBlank() && email.isNotBlank() &&
                            password.isNotBlank() && confirmPassword.isNotBlank() &&
                            password == confirmPassword
                },
                label = { Text("Full Name") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Name") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = email,
                onValueChange = {
                    email = it
                    isSignUpEnabled = name.isNotBlank() && email.isNotBlank() &&
                            password.isNotBlank() && confirmPassword.isNotBlank() &&
                            password == confirmPassword
                },
                label = { Text("Email") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = password,
                onValueChange = {
                    password = it
                    isSignUpEnabled = name.isNotBlank() && email.isNotBlank() &&
                            password.isNotBlank() && confirmPassword.isNotBlank() &&
                            password == confirmPassword
                },
                label = { Text("Password") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password") },
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    isSignUpEnabled = name.isNotBlank() && email.isNotBlank() &&
                            password.isNotBlank() && confirmPassword.isNotBlank() &&
                            password == confirmPassword
                },
                label = { Text("Confirm Password") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Confirm Password") },
                trailingIcon = {
                    IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                        Icon(
                            if (isConfirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                },
                visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.signUp(name, email, password) },
                enabled = isSignUpEnabled && uiState !is UiState.Loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                if (uiState is UiState.Loading) {
                    CircularProgressIndicator(color = Color.White)
                } else {
                    Text("Sign Up", fontSize = 18.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Already have an account?", color = MaterialTheme.colorScheme.primary)
                TextButton(onClick = { navController.popBackStack() }) {
                    Text(
                        "Log In",
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline
                    )
                }
            }
        }
        LaunchedEffect(uiState) {
            when (uiState) {
                is UiState.Success -> navController.navigate(Screen.TripList.route)
                is UiState.Error -> {
                    Toast.makeText(
                        context,
                        (uiState as UiState.Error).message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {}
            }
        }
        AnimatedVisibility(
            visible = isSignUpEnabled,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        ) {
            Text(
                "Ready to explore the world!",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}