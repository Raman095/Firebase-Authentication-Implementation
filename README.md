# 🔐 Firebase Authentication App (Jetpack Compose)

## 📌 Introduction

This project is a modern Android authentication system built using **Jetpack Compose** and **Firebase Authentication**.

It demonstrates a clean implementation of user authentication including:

- Email & Password Login
- User Registration
- Password Reset (Forgot Password)
- Google Sign-In

The application follows **MVVM architecture** and uses **Material 3** for modern UI design.

This project focuses on clean architecture, proper state management, and real-world authentication flow implementation.

---

## 🚀 Features

- 🔑 Login with Email & Password  
- 🆕 User Registration  
- 🔁 Password Reset via Email  
- 🔓 Google Sign-In Integration  
- 🧠 MVVM Architecture  
- 🎨 Material 3 UI  
- 📱 Fully built using Jetpack Compose  

---

## 📸 Screenshots

> ⚠️ Create a folder named `images` in your project and place your screenshots there.

### 🔐 Google Account Picker
![Google Account Picker](images/google_account_picker.jpg)

### 🔁 Forgot Password Dialog
![Forgot Password](images/forgot_password.jpg)

### 🆕 Sign Up Screen
![Sign Up](images/sign_up.jpg)

### 🔑 Login Screen
![Login](images/login.jpg)

---

## 🏗️ Architecture

This project follows **MVVM (Model-View-ViewModel)** architecture:

- **UI Layer** → Built using Jetpack Compose  
- **ViewModel** → Handles UI state and business logic  
- **Firebase Layer** → Manages authentication operations  

This ensures:

- Clear separation of concerns  
- Scalable code structure  
- Easy maintenance  
- Better testability  

---

## 🛠️ Tech Stack

- **Kotlin**
- **Jetpack Compose**
- **Material 3**
- **Firebase Authentication**
  - Email & Password Authentication
  - Google Sign-In Authentication
- **MVVM Architecture**

---

## 🔧 Firebase Setup

To run this project:

1. Create a Firebase project.
2. Enable:
   - Email/Password Authentication
   - Google Sign-In Authentication
3. Add SHA-1 and SHA-256 keys in Firebase Console.
4. Download `google-services.json`.
5. Place it inside the `app/` directory.
6. Sync project with Gradle.

---

## 👨‍💻 Author

Developed as part of Android development practice using modern Android tools and architecture principles.
