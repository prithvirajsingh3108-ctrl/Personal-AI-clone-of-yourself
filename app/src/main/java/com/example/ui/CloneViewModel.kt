package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CloneViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = CloneRepository(db.cloneDao())

    val profileState: StateFlow<Profile?> = repository.profile.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val projectsState: StateFlow<List<Project>> = repository.projects.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val achievementsState: StateFlow<List<Achievement>> = repository.achievements.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val chatMessagesState: StateFlow<List<ChatMessage>> = repository.chatMessages.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _isGeneratingResponse = MutableStateFlow(false)
    val isGeneratingResponse: StateFlow<Boolean> = _isGeneratingResponse.asStateFlow()

    init {
        // Automatic onboarding check: Check if profile is empty, if so, seed mock details automatically
        viewModelScope.launch(Dispatchers.IO) {
            val existingProfile = db.cloneDao().getProfile().stateIn(viewModelScope).value
            if (existingProfile == null) {
                repository.resetToDefaultSeed()
            }
        }
    }

    fun resetToPrithiviDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.resetToDefaultSeed()
        }
    }

    fun updateProfile(
        name: String,
        title: String,
        email: String,
        bio: String,
        skills: String,
        rawResumeText: String,
        personalityArchetype: String = "Formal",
        customStyleInstructions: String = ""
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveProfile(
                Profile(
                    name = name.trim(),
                    title = title.trim(),
                    email = email.trim(),
                    bio = bio.trim(),
                    skills = skills.trim(),
                    rawResumeText = rawResumeText.trim(),
                    personalityArchetype = personalityArchetype,
                    customStyleInstructions = customStyleInstructions
                )
            )
        }
    }

    fun addProject(
        title: String,
        description: String,
        role: String,
        technologies: String,
        url: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addProject(
                Project(
                    title = title.trim(),
                    description = description.trim(),
                    role = role.trim(),
                    technologies = technologies.trim(),
                    url = url.trim()
                )
            )
        }
    }

    fun deleteProject(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteProject(id)
        }
    }

    fun addAchievement(
        title: String,
        issuerOrComp: String,
        date: String,
        description: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addAchievement(
                Achievement(
                    title = title.trim(),
                    issuerOrComp = issuerOrComp.trim(),
                    date = date.trim(),
                    description = description.trim()
                )
            )
        }
    }

    fun deleteAchievement(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAchievement(id)
        }
    }

    fun sendRecruiterMessage(messageText: String) {
        if (messageText.isBlank()) return
        
        viewModelScope.launch(Dispatchers.IO) {
            // Write User query into Room
            repository.addChatMessage(
                ChatMessage(
                    sender = "USER",
                    message = messageText.trim()
                )
            )

            // Enable visual loader
            _isGeneratingResponse.value = true

            // Reach Gemini API and stream or gather answer
            val answer = repository.generateResponseFromClone(messageText)

            // Write Clone answer into Room
            repository.addChatMessage(
                ChatMessage(
                    sender = "CLONE",
                    message = answer
                )
            )

            // Disable loader
            _isGeneratingResponse.value = false
        }
    }

    fun clearChatHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearChat()
            val profileName = profileState.value?.name ?: "Prithivi Raj Singh"
            repository.addChatMessage(
                ChatMessage(
                    sender = "CLONE",
                    message = "Hi! I have loaded the custom AI Clone profile of $profileName. You can ask me questions about my tech skills, project experience, achievements, or ask for my contact email. I'm ready for the interview!"
                )
            )
        }
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CloneViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CloneViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
