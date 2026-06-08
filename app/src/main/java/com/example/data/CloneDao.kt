package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CloneDao {
    // Profile
    @Query("SELECT * FROM profile WHERE id = 1 LIMIT 1")
    fun getProfile(): Flow<Profile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: Profile)

    // Projects
    @Query("SELECT * FROM projects ORDER BY id DESC")
    fun getAllProjects(): Flow<List<Project>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: Project)

    @Query("DELETE FROM projects WHERE id = :id")
    suspend fun deleteProjectById(id: Int)

    // Achievements
    @Query("SELECT * FROM achievements ORDER BY id DESC")
    fun getAllAchievements(): Flow<List<Achievement>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAchievement(achievement: Achievement)

    @Query("DELETE FROM achievements WHERE id = :id")
    suspend fun deleteAchievementById(id: Int)

    // Chat Messages
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getChatMessages(): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(message: ChatMessage)

    @Query("DELETE FROM chat_messages")
    suspend fun clearChat()

    // Resets & Clears for Profile Seed
    @Query("DELETE FROM profile")
    suspend fun clearProfile()

    @Query("DELETE FROM projects")
    suspend fun clearProjects()

    @Query("DELETE FROM achievements")
    suspend fun clearAchievements()
}
