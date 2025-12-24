# 📱 LearningAI – Interview MCQ App (Android)

LearningAI is an Android application built using Jetpack Compose and MVVM architecture.  
This app helps users practice interview questions (MCQ based) with clean UI and proper state management.

---

## 🚀 Features

- 🏠 Home screen with feature buttons
- ❓ Interview Questions (MCQ)
- 👉 Option selection handling
- ⏭️ Next question logic
- 🧠 ViewModel based state management
- 🔄 StateFlow for reactive UI updates
- 🧭 Navigation using NavController

---

## 🛠 Tech Stack

- Language: Kotlin  
- UI: Jetpack Compose  
- Architecture: MVVM  
- State Management: StateFlow  
- Navigation: Navigation Compose  

---

## 🧱 Project Structure

`text
com.example.learningai
│
├── home
│   ├── HomeSCR.kt
│   ├── InterviewScreen.kt
│   ├── InterviewOptionButton.kt
│   ├── QuestionCard.kt
│   └── OptionItem.kt
│
├── viewmodel
│   └── InterviewViewModel.kt
│
├── nav
│   ├── AppNavGraph.kt
│   └── Routes.kt
│
├── model
│   └── Questions.kt
│
└── MainActivity.kt
