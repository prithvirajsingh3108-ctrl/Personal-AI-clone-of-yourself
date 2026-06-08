package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile")
data class Profile(
    @PrimaryKey val id: Int = 1,
    val name: String,
    val title: String,
    val email: String,
    val bio: String,
    val skills: String, // comma-separated strings
    val rawResumeText: String,
    val personalityArchetype: String = "Formal",
    val customStyleInstructions: String = ""
)

@Entity(tableName = "projects")
data class Project(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val role: String,
    val technologies: String, // comma-separated
    val url: String
)

@Entity(tableName = "achievements")
data class Achievement(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val issuerOrComp: String, // Company, university or issuer
    val date: String,
    val description: String
)

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sender: String, // "USER" or "CLONE"
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)
