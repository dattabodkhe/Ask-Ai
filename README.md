# 🚀 LearningAI – AI-Driven Collaborative Learning Ecosystem

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-purple.svg?style=flat&logo=kotlin)](https://kotlinlang.org/)
[![Compose](https://img.shields.io/badge/Jetpack_Compose-Material_3-green.svg?style=flat&logo=android)](https://developer.android.com/jetpack/compose)
[![Firebase](https://img.shields.io/badge/Backend-Firebase-orange.svg?style=flat&logo=firebase)](https://firebase.google.com/)
[![AI](https://img.shields.io/badge/AI-Gemini_Pro-blue.svg?style=flat&logo=google-gemini)](https://deepmind.google/technologies/gemini/)

**LearningAI** is a sophisticated Android application that merges **Generative AI** with **Social Learning**. It enables a real-time, role-based classroom environment where AI-generated assessments are integrated directly into a collaborative chat workflow.

---
<p align="center">
  <img src="images/learning_ai.png" width="100%" alt="LearningAI Project Showcase">
</p>

## ✨ Key Features

### 🤖 AI-Powered Assessment Engine
* **On-Demand Quiz Generation:** Leverages the **Gemini Pro API** to generate context-aware MCQs based on specific subjects and difficulty levels.
* **Smart Prompting:** A dynamic "AI Invite Card" system within the chat to trigger quiz generation for the group instantly.

### 🏛 Real-time Classroom & Collaboration
* **Synchronous Chat:** Powered by **Firebase Firestore**, providing real-time messaging with sub-second latency.
* **Role-Based Access Control (RBAC):** Distinct workflows and permissions for **Teachers** (Admin) and **Students** (Members).
* **Granular Security:** Professional **Firestore Security Rules** ensuring that classroom data, messages, and results are only accessible to verified members.

### 📱 Modern Mobile UX/UI
* **Material 3 Design:** A fully responsive UI utilizing **Dynamic Color** palettes, glassmorphic components, and smooth animations.
* **Type-Safe Navigation:** A robust navigation graph handling complex, multi-parameter dynamic routes without crashes.
* **Contact Integration:** Seamlessly add friends directly from local device contacts into virtual classrooms.

---

## 🏗 Tech Stack & Architecture

The app is built using **Clean Architecture** principles to ensure high maintainability and scalability.



* **UI:** Jetpack Compose (Declarative UI)
* **Architecture:** MVVM (Model-View-ViewModel)
* **State Management:** StateFlow & SharedFlow (Reactive programming)
* **Database & Auth:** Firebase Firestore & Firebase Authentication
* **AI Logic:** Google Gemini AI API
* **Serialization:** Gson for complex object parsing

---

## 📂 System Design (Package Structure)

```text
com.example.learningai
├── 📁 classroom     # Real-time chat, AI Invites, Member Management
├── 📁 home          # Dashboard with dynamic subject modules
├── 📁 model         # Domain models (Message, User, QuizResult)
├── 📁 nav           # Type-safe Navigation Graph & Route Definitions
├── 📁 repository    # Abstracted Data Layer (Firebase/API)
└── 📁 user          # Authentication and User Profile Logic
🔒 Security Implementation
Unlike basic apps, LearningAI implements professional-grade security:

Member Validation: Custom rules validate user existence in a classroom before allowing data access.

Admin Constraints: Restricts quiz management and classroom settings to authorized creators.

Data Integrity: Implements server-side timestamps and atomic operations.

👨‍💻 Author
Datta Bodkhe Full-Stack Android Developer

🌐 github.dattabodkhe.com | 📧 Email : bodkhedatta77@gmail.com

💡 Project Status: This project reflects a complete engineering lifecycle—from integrating LLMs (Gemini) to managing complex state and secure cloud backends.
