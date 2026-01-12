package com.example.loginjcauth

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun HomeScreen(navController: NavHostController) {
    val context = LocalContext.current
    val webClientId = "526728699004-6qm44jpqu0c4bppamrsasvnm9l87mdf1.apps.googleusercontent.com"

    Box(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Welcome to Home Screen",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    Firebase.auth.signOut()
                    navController.navigate("login") {
                        popUpTo("home") {inclusive = true}
                    }
                }
            ) {
                Text(
                    text = "Logout"
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    googleSignOut(context, webClientId) {
                        Toast.makeText(context, "Logout Successful", Toast.LENGTH_SHORT).show()
                        navController.navigate("login") {
                            popUpTo("home") {inclusive = true}
                        }
                    }
                }
            ) {
                Text(
                    text = "Logout from Google"
                )
            }
        }
    }
}

fun googleSignOut(context: Context, webClientId: String, onComplete: ()-> Unit ) {
    Firebase.auth.signOut() // logout from firebase


    // logout from Google
    val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(webClientId)
        .requestEmail()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions)
    googleSignInClient.signOut().addOnCompleteListener { // This logs the user out from Google — meaning the next time the user opens the Google Sign-In UI, they will have to pick an account again.
        onComplete()                // This is asynchronous (takes time), so we attach an addOnCompleteListener to know when it’s actually finished.
    }
}        // onComplete() -> when we call the GoogleSignOut, which we did above inside "onClick = {}", the code inside that is the part of onComplete() function.
         // Basically, after the Sign Out process is completed, .addOnCompleteListener checks if the process is completed, only when the sign out is done, onComplete() function runs and this function contains that Toast and navigate process (defined inside the button "onClick").