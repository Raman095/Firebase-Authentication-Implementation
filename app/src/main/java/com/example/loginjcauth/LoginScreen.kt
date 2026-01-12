package com.example.loginjcauth

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth

@Composable
fun LoginScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var forgotPasswordDialogBox by remember {mutableStateOf(false)}
    val context = LocalContext.current

    val googleSignInOptions = remember {
        // GoogleSignInOptions: Defines what info you want from the user.
        // DEFAULT_SIGN_IN: Basic profile info.
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            // .requestIdToken(...): Requests an ID token (needed to authenticate with Firebase).
            .requestIdToken("get web=client from firebase.")
            // .requestEmail(): Requests the user’s email.
            .requestEmail()
            .build()
    }

    // prepares the rules and prepares an intent (doesn't create intent), intent is just a file that contains rules like what info you want from the user account, here we will collect, profile pic, user name and email, because we used .DEFAULT_SIGN_IN
    val googleSignInClient = remember {
        GoogleSignIn.getClient(context, googleSignInOptions)
    }

    val launcher = rememberLauncherForActivityResult( // rememberLauncherForActivityResult: A Compose helper to launch an external activity (Google’s sign-in screen) and handle the result.
        contract = ActivityResultContracts.StartActivityForResult() // <-- This contract tells the launcher that we are starting an Activity and expect a result back (in our case, the Google Sign-In result Intent). Without this, the launcher wouldn't know how to launch the activity or handle its result.
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data) // .getSignedInAccountFromIntent: extracts data from intent send by google after selecting account, at first data is in result.data, after extracting, it is stored in task.
        try {
            val account = task.result
            val credential = GoogleAuthProvider.getCredential(account.idToken, null) // gets token to match with firebase
            Firebase.auth.signInWithCredential(credential) // sign in with credential
                .addOnCompleteListener {
                    if(task.isSuccessful) {
                        Toast.makeText(context, "Google Sign-in Successful", Toast.LENGTH_SHORT).show()
                        navController.navigate("home") {
                            popUpTo("login") {inclusive = true}
                        }
                    } else {
                        Toast.makeText(context, "Google Sign-in failed", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: Exception) {
            Toast.makeText(context, "Google Sign-in failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Login",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Email Entering Field
            OutlinedTextField(
                value = email,
                onValueChange = {email = it},
                label = {Text("Email")},
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Password Entering Field
            OutlinedTextField(
                value = password,
                onValueChange = {password = it},
                label = {Text("Password")},
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Login Button
            val context = LocalContext.current
            Button(
                onClick = {
                    Firebase.auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if(task.isSuccessful) {
                                Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()
                                navController.navigate("home") {
                                    popUpTo("login") {inclusive = true}
                                }
                            }
                            else {
                                Toast.makeText(context, task.exception?.message?: "Login failed"
                                    , Toast.LENGTH_SHORT).show()
                            }
                        }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Login"
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Google Sign In
            AndroidView(modifier = Modifier.fillMaxWidth(),
                factory = { context ->
                    SignInButton(context).apply {
                        setSize(SignInButton.SIZE_WIDE)
                        setOnClickListener { //It lets us tell a UI element (like a button) what to do when the user clicks it.
                            val signInIntent = googleSignInClient.signInIntent // creates intent keeping the rules in place, .signInIntent -> Gets an Intent to start the Google Sign In flow by calling Activity.d
                            launcher.launch(signInIntent) // it launches the Google Sign-in UI, all these intent rules, intent preparation and intent creation happens before Google Sign-in UI is launched, they doesn't contain data, just intent is created. Data is sent by google, that data is stored in a new intent created by google (but follow the rules of previous intent created by signInIntent), and stored in the variable task (see above in the code).
                        }
                    }
                })

            // Forgot Password Button
            if(forgotPasswordDialogBox) {
                var resetEmail by remember { mutableStateOf("")}
                val context = LocalContext.current

                AlertDialog(
                    title = {Text(text = "Forgot Password")},
                    text = {
                        OutlinedTextField(
                            value = resetEmail,
                            onValueChange = {resetEmail = it},
                            label = {Text(text = "Email")},
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                if(resetEmail.isNotBlank()) {
                                    Firebase.auth.sendPasswordResetEmail(resetEmail)
                                        .addOnCompleteListener { task ->
                                            if(task.isSuccessful) {
                                                Toast.makeText(context, "Reset link sent to your email", Toast.LENGTH_SHORT).show()
                                                forgotPasswordDialogBox = false
                                            }
                                            else {
                                                Toast.makeText(context, "Registered Email not Found", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                }
                                else {
                                    Toast.makeText(context, "Please enter your registered email", Toast.LENGTH_SHORT).show()
                                }
                            }
                        ) {
                            Text(text = "Submit")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { forgotPasswordDialogBox = false}
                        ) {
                            Text(text = "Cancel")
                        }
                    },
                    onDismissRequest = { forgotPasswordDialogBox = false }
                )
            }
            TextButton(
                onClick = { forgotPasswordDialogBox = true}
            ) {
                Text(
                    text = "Forgot password?"
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Doesn't have an account Button
            TextButton(
                onClick = {
                    navController.navigate("signup") {
                        popUpTo("login") { inclusive = true}
                    }
                }
            ) {
                Text(
                    text = "Doesn't have an account? Sign Up"
                )
            }
        }
    }
}
