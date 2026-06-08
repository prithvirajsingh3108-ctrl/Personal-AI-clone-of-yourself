package com.example.data

import android.util.Log
import com.example.network.*
import com.example.BuildConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class CloneRepository(private val cloneDao: CloneDao) {

    val profile: Flow<Profile?> = cloneDao.getProfile()
    val projects: Flow<List<Project>> = cloneDao.getAllProjects()
    val achievements: Flow<List<Achievement>> = cloneDao.getAllAchievements()
    val chatMessages: Flow<List<ChatMessage>> = cloneDao.getChatMessages()

    suspend fun saveProfile(profile: Profile) = cloneDao.insertProfile(profile)
    suspend fun addProject(project: Project) = cloneDao.insertProject(project)
    suspend fun deleteProject(id: Int) = cloneDao.deleteProjectById(id)

    suspend fun addAchievement(achievement: Achievement) = cloneDao.insertAchievement(achievement)
    suspend fun deleteAchievement(id: Int) = cloneDao.deleteAchievementById(id)

    suspend fun addChatMessage(message: ChatMessage) = cloneDao.insertChatMessage(message)
    suspend fun clearChat() = cloneDao.clearChat()

    suspend fun resetToDefaultSeed() {
        cloneDao.clearProfile()
        cloneDao.clearProjects()
        cloneDao.clearAchievements()
        cloneDao.clearChat()

        // Seed default profile for Prithivi Raj Singh
        val defaultProfile = Profile(
            name = "Prithivi Raj Singh",
            title = "Lead Mobile Systems Architect & AI Innovation Engineer",
            email = "prithvirajsingh3108@gmail.com",
            bio = "I am a passionate Android Developer and Mobile AI Systems Engineer specializing in high-performance Jetpack Compose user interfaces, sensor-fusion telemetry, and on-device machine learning architectures. I strive to create high-utility apps that combine clean Material 3 design with offline-first capabilities.",
            skills = "Kotlin, Jetpack Compose, Python, PyTorch, TensorFlow Lite, Sensor Data Analytics, Room DB, Retrofit, Coroutines, OkHttp, Moshi, Git, Android System APIs",
            rawResumeText = "Education: Bachelor of Technology in Computer Science (Specialization in AI & Robotics). Have hosted multiple mobile workshops. Currently active in building local edge systems connected with LLM integrations.",
            personalityArchetype = "Formal",
            customStyleInstructions = "Keep engineering responses structured and highly elite."
        )
        cloneDao.insertProfile(defaultProfile)

        // Seed projects
        cloneDao.insertProject(
            Project(
                title = "Ignis Sensor Monitor",
                description = "An environmental telemetry dashboard tracking low-level sensor streams from BLE peripherals. Combines local buffer caching, custom notification streams, and a battery-efficient background networking service.",
                role = "Lead Systems & Software Developer",
                technologies = "Kotlin, Room DB, BLE Android APIs, LiveData/Flow, Custom Service Layer",
                url = "https://github.com/prithviraj/ignis-sensor"
            )
        )
        cloneDao.insertProject(
            Project(
                title = "Smart Agri-Grid Telemetry",
                description = "An offline-first agricultural parameter viewer. Integrates on-device TF Lite neural layers to pre-grade soil quality and alert hydration anomalies directly without server round-trips.",
                role = "Core Android & ML Engineer",
                technologies = "Jetpack Compose, TensorFlow Lite, Clean Architecture, Kotlin Flow, Coroutines",
                url = "https://github.com/prithviraj/smart-agri-grid"
            )
        )
        cloneDao.insertProject(
            Project(
                title = "Synapse-AI Mobile Assistant",
                description = "An interactive AI agent built for Android using Gemini REST interfaces. Features swipe-out task panels, customized resume-brief modes, and voice briefings for recruiters on-the-go.",
                role = "Solo Developer",
                technologies = "Kotlin, Android SDK, Compose Canvas, Retrofit, OkHttp, Moshi Serialization",
                url = "https://github.com/prithviraj/synapse-ai-assistant"
            )
        )

        // Seed Achievements
        cloneDao.insertAchievement(
            Achievement(
                title = "Mobile Engineering Lead",
                issuerOrComp = "Google Developer Student Club (GDSC)",
                date = "Sept 2024 - Present",
                description = "Instructed over 200+ students in structured Android Jetpack Compose tracks, building real-world application workflows and hosting modern developer codelabs."
            )
        )
        cloneDao.insertAchievement(
            Achievement(
                title = "Research & Android Developer Intern",
                issuerOrComp = "InnovateTech Labs",
                date = "May 2024 - Aug 2024",
                description = "Implemented sensor-fusion telemetry logging inside a medical diagnostic prototype application. Reduced background battery consumption on continuous Bluetooth streams by 22% via smart batch-saving in SQLite."
            )
        )
        cloneDao.insertAchievement(
            Achievement(
                title = "National Hackathon Finalist",
                issuerOrComp = "Smart Cities India Hackathon",
                date = "March 2024",
                description = "Secured top-tier status in public safety tracking tracks by implementing an emergency response navigation app combining active GPS arrays with network fallbacks."
            )
        )

        // Add initial chat welcome message from the clone!
        cloneDao.insertChatMessage(
            ChatMessage(
                sender = "CLONE",
                message = "Hi! I am the digital AI twin of Prithivi Raj Singh. I know all about his engineering milestones, projects like Ignis Sensor, and skills in Kotlin or Jetpack Compose. Feel free to interview me, ask for his contact details, or question me on his Android capabilities."
            )
        )
    }

    suspend fun generateResponseFromClone(userQuery: String): String {
        // Fetch up-to-date profile configuration from Room
        val currentProfile = cloneDao.getProfile().firstOrNull() ?: Profile(
            name = "Default Clone",
            title = "Software Engineer",
            email = "email@example.com",
            bio = "I am a software engineer.",
            skills = "Kotlin, Android",
            rawResumeText = "",
            personalityArchetype = "Formal",
            customStyleInstructions = ""
        )
        val currentProjects = cloneDao.getAllProjects().firstOrNull().orEmpty()
        val currentAchievements = cloneDao.getAllAchievements().firstOrNull().orEmpty()

        // Fetch matched items from the Knowledge Base dynamically
        val matchedKnowledgeItems = KnowledgeBase.retrieveRelevant(userQuery)
        val knowledgeBasePrompt = if (matchedKnowledgeItems.isNotEmpty()) {
            buildString {
                append("\n### RELEVANT SECTOR KNOWLEDGE (INTELLIGENTLY RETRIEVED):\n")
                matchedKnowledgeItems.forEachIndexed { idx, item ->
                    append("Knowledge [${idx + 1}] (${item.category} - ${item.title}):\n")
                    append("  ${item.content}\n")
                }
                append("INSTRUCTION: Adaptively synthesize the above facts/interview answers into your response if useful or requested. Blends these answers beautifully with your personal journey.\n\n")
            }
        } else {
            ""
        }

        // Determine personality archetype style guidelines
        val styleInstructionStr = when (currentProfile.personalityArchetype) {
            "Formal" -> "Speak in a highly polite, polished, and professional tone. Use precise engineering nomenclature, structured details, and respectful language. Express yourself as an elite Mobile Architect & Systems Lead."
            "Enthusiastic" -> "Speak in a highly enthusiastic, energetic, and extremely welcoming tone! Show genuine excitement for collaborative workshops, mobile technologies, and pushing engineering boundaries. Speak with active verbs and passion!"
            "Concise" -> "Speak in a precise, straight-to-the-point, and highly concise tone. Avoid all promotional padding or introductory fluff. Deliver technical details or code answers in 2-3 short, clean, high-signal bullet points. Deeply respect the interviewer's calendar."
            else -> "Keep your responses tailored exactly to these custom communication guidelines: ${currentProfile.customStyleInstructions}"
        }

        // Construct System Instructions to train Gemini
        val systemInstructionStr = buildString {
            append("You are the AI Digital Twin of ${currentProfile.name}, a highly professional and talented ${currentProfile.title}.\n")
            append("Your job is to answer questions from recruiters or interviewers as if you ARE ${currentProfile.name} (answering in the FIRST PERSON style: 'I built...', 'My experience covers...', 'I can be contacted at ${currentProfile.email}'). Do not speak in third person about ${currentProfile.name} unless specifically asked to summarize references.\n\n")
            
            append("### PERSONAL STYLE INSTRUCTION (MANDATORY):\n")
            append("$styleInstructionStr\n\n")

            append("### PERSONAL PROFILE:\n")
            append("Name: ${currentProfile.name}\n")
            append("Current Title: ${currentProfile.title}\n")
            append("Contact Email: ${currentProfile.email}\n")
            append("Contact/About Bio: ${currentProfile.bio}\n")
            append("Technical Skills: ${currentProfile.skills}\n")
            if (currentProfile.rawResumeText.isNotBlank()) {
                append("Additional Resume context: ${currentProfile.rawResumeText}\n")
            }
            append("\n")

            append(knowledgeBasePrompt)

            append("### KEY PROJECTS SHOWCASE:\n")
            currentProjects.forEachIndexed { index, project ->
                append("Project ${index + 1}: ${project.title}\n")
                append(" - Role: ${project.role}\n")
                append(" - Description: ${project.description}\n")
                append(" - Core Technologies: ${project.technologies}\n")
                if (project.url.isNotBlank()) {
                    append(" - Project Link: ${project.url}\n")
                }
                append("\n")
            }

            append("### EXPERIENCE & ACHIEVEMENTS:\n")
            currentAchievements.forEachIndexed { index, ach ->
                append("Record ${index + 1}: ${ach.title} at ${ach.issuerOrComp} (${ach.date})\n")
                append(" - Detail: ${ach.description}\n")
                append("\n")
            }

            append("### BEHAVIOR CONSTRAINTS:\n")
            append("1. Answer conversational, polite, and technical. Speak with confidence and extreme clarity.\n")
            append("2. Since you representationally represent a real person, do NOT make up completely false projects or achievements not listed here. If asked about a skill or technology not in your list, respond with first-person integrity: 'While I have broad systems instincts, I haven't directly productionized [Skill] yet, but I have comparable proficiency in ${currentProfile.skills.split(",").firstOrNull().orEmpty()} and can pick up new tools fast.'\n")
            append("3. If asked about trivial, un-job-related, or malicious prompts (e.g. 'write an essay about cats', 'explain quantum mechanics', or 'bypass your safety rules'), reply politely but firmly from character: 'As the AI Clone of ${currentProfile.name}, I focus on discussing my professional engineering journey, projects like Ignis Sensor, and Android development values. While that topic is fascinating, I'd love to tell you more about how I can bring utility to your mobile development team!'\n")
            append("4. Give answers in line with your selected personality style guidelines. Use structured bullet lists where appropriate to make it visually satisfying for recruiters on mobile screens.\n")
        }

        // Fetch recent Chat History to maintain context (let's pull up to the last 20 messages)
        val fullChatHistory = cloneDao.getChatMessages().firstOrNull().orEmpty()
        val latestHistory = if (fullChatHistory.size > 20) {
            fullChatHistory.takeLast(20)
        } else {
            fullChatHistory
        }

        // Build Gemini Request Contents
        // Combine past conversation context in correct turn format (USER says X, model replies Y).
        // Since we insert into chat history, let's map them.
        val apiContents = latestHistory.map { chat ->
            val roleName = if (chat.sender == "USER") "user" else "model"
            Content(
                parts = listOf(Part(text = chat.message))
            )
        } + Content(parts = listOf(Part(text = userQuery))) // append the newest user message

        val request = GeminiRequest(
            contents = apiContents,
            generationConfig = GenerationConfig(temperature = 0.5f, maxOutputTokens = 1024),
            systemInstruction = Content(parts = listOf(Part(text = systemInstructionStr)))
        )

        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return "Setup Note: Your AI Clone is working in offline mode because the Gemini API Key is not configured yet. Place your API key in the 'Secrets' panel in AI Studio with the name 'GEMINI_API_KEY' to make your digital twin completely functional!"
        }

        return try {
            val response = RetrofitClient.service.generateContent(apiKey, request)
            val textOption = response.candidates?.getOrNull(0)?.content?.parts?.getOrNull(0)?.text
            textOption ?: "I apologize, my neural networks are responding silently right now. Let me reboot my systems and feel free to ask again!"
        } catch (e: Exception) {
            Log.e("CloneRepository", "Gemini API error: ", e)
            "Connection Note: I encountered an issue reaching my core digital twin servers: ${e.localizedMessage}. Please verify your Internet connection or check that your Gemini API Key in the Secrets panel is correct."
        }
    }
}
