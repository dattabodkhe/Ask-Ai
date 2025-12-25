package com.example.learningai.model

val notesMap = mapOf(

    "app_dev" to """
Jetpack Compose
---------------
Jetpack Compose is Android’s modern UI toolkit.
It uses declarative UI with Kotlin.

MVVM Architecture
-----------------
MVVM separates UI from business logic.
ViewModel survives configuration changes.

StateFlow
---------
StateFlow is a state-holder observable flow.
Used heavily with Compose.
""".trimIndent(),

    "web_dev" to """
HTML
----
HTML structures web pages.

CSS
---
CSS styles web pages.

JavaScript
----------
JavaScript adds interactivity.
""".trimIndent(),

    "dsa" to """
Arrays
------
Collection of elements.

Linked List
-----------
Nodes connected linearly.

Queue
-----
FIFO data structure.
""".trimIndent(),

    "ai_ml" to """
Machine Learning
----------------
Machines learn from data.

Neural Networks
---------------
Inspired by human brain neurons.
""".trimIndent()
)