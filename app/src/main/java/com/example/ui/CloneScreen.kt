package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.Achievement
import com.example.data.ChatMessage
import com.example.data.Project

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CloneScreen(
    viewModel: CloneViewModel,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.profileState.collectAsStateWithLifecycle()
    val projects by viewModel.projectsState.collectAsStateWithLifecycle()
    val achievements by viewModel.achievementsState.collectAsStateWithLifecycle()
    val chatMessages by viewModel.chatMessagesState.collectAsStateWithLifecycle()
    val isGenerating by viewModel.isGeneratingResponse.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableIntStateOf(0) }

    // Dialog state
    var showAddProjectDialog by remember { mutableStateOf(false) }
    var showAddAchievementDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Surface(
                tonalElevation = 1.dp,
                shadowElevation = 0.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Dynamic initials avatar (e.g. "Prithivi Raj Singh" -> "PS")
                            val initials = remember(profile?.name) {
                                val parts = (profile?.name ?: "Prithivi Singh").split(" ")
                                if (parts.size >= 2) {
                                    "${parts[0].firstOrNull() ?: 'P'}${parts.last().firstOrNull() ?: 'S'}"
                                } else {
                                    "${profile?.name?.firstOrNull() ?: 'P'}"
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF005AC1)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = initials.uppercase(),
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                            Column {
                                Text(
                                    text = "${profile?.name ?: "Prithivi"} AI",
                                    fontWeight = FontWeight.SemiBold,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color(0xFF1B1B1F)
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    modifier = Modifier.padding(top = 2.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(7.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF10B981))
                                    )
                                    Text(
                                        text = "Always Online",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                        color = Color(0xFF44474E)
                                    )
                                }
                            }
                        }

                        // Reset action icon to quickly load seeded default demo values
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { viewModel.resetToPrithiviDetails() },
                                modifier = Modifier.testTag("reset_prithviraj_profile_button"),
                                colors = IconButtonDefaults.iconButtonColors(contentColor = Color(0xFF005AC1))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Reset AI Clone credentials"
                                )
                            }
                        }
                    }
                    HorizontalDivider(color = Color(0xFFE1E2EC), thickness = 1.dp)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Elegant tab layout for swift mode switches
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Home, contentDescription = null, sizeModifier(18))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Interview Clone", fontWeight = FontWeight.SemiBold)
                        }
                    },
                    modifier = Modifier.testTag("interview_clone_tab")
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Settings, contentDescription = null, sizeModifier(18))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Customize Twin", fontWeight = FontWeight.SemiBold)
                        }
                    },
                    modifier = Modifier.testTag("customize_twin_tab")
                )
            }

            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    if (targetState > initialState) {
                        slideInHorizontally { width -> width } + fadeIn() togetherWith
                                slideOutHorizontally { width -> -width } + fadeOut()
                    } else {
                        slideInHorizontally { width -> -width } + fadeIn() togetherWith
                                slideOutHorizontally { width -> width } + fadeOut()
                    }.using(SizeTransform(clip = false))
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                label = "tab_animation"
            ) { currentTab ->
                when (currentTab) {
                    0 -> RecruiterChatTab(
                        messages = chatMessages,
                        isGenerating = isGenerating,
                        onSendMessage = { viewModel.sendRecruiterMessage(it) },
                        onClearChat = { viewModel.clearChatHistory() }
                    )
                    1 -> TrainingCenterTab(
                        name = profile?.name.orEmpty(),
                        title = profile?.title.orEmpty(),
                        email = profile?.email.orEmpty(),
                        bio = profile?.bio.orEmpty(),
                        skills = profile?.skills.orEmpty(),
                        personalityArchetype = profile?.personalityArchetype ?: "Formal",
                        customStyleInstructions = profile?.customStyleInstructions.orEmpty(),
                        rawText = profile?.rawResumeText.orEmpty(),
                        projects = projects,
                        achievements = achievements,
                        onUpdateProfile = { nameVal, titleVal, emailVal, bioVal, skillsVal, rawVal, archVal, customVal ->
                            viewModel.updateProfile(nameVal, titleVal, emailVal, bioVal, skillsVal, rawVal, archVal, customVal)
                        },
                        onDeleteProject = { viewModel.deleteProject(it) },
                        onDeleteAchievement = { viewModel.deleteAchievement(it) },
                        onAddProjectClick = { showAddProjectDialog = true },
                        onAddAchievementClick = { showAddAchievementDialog = true }
                    )
                }
            }
        }
    }

    // Modal dialogs for clean structural updates
    if (showAddProjectDialog) {
        AddProjectDialog(
            onDismiss = { showAddProjectDialog = false },
            onConfirm = { title, desc, role, tech, link ->
                viewModel.addProject(title, desc, role, tech, link)
                showAddProjectDialog = false
            }
        )
    }

    if (showAddAchievementDialog) {
        AddAchievementDialog(
            onDismiss = { showAddAchievementDialog = false },
            onConfirm = { title, org, date, desc ->
                viewModel.addAchievement(title, org, date, desc)
                showAddAchievementDialog = false
            }
        )
    }
}

@Composable
fun RecruiterChatTab(
    messages: List<ChatMessage>,
    isGenerating: Boolean,
    onSendMessage: (String) -> Unit,
    onClearChat: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var inputMessage by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    // Automatically auto-scrolls to the newest bubbles
    LaunchedEffect(messages.size, isGenerating) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FF)) // Professional Polish light background
    ) {
        // Status Indicators Header - Matching "Always Online" status and "Clear Chat" triggers
        Surface(
            tonalElevation = 0.dp,
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .background(Color(0xFF10B981), CircleShape)
                    )
                    Text(
                        text = "AI CLONE CORE ACTIVE (GEMINI-3.5-FLASH)",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        ),
                        color = Color(0xFF44474E)
                    )
                }
                Text(
                    text = "CLEAR HISTORY",
                    color = Color(0xFF005AC1),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    ),
                    modifier = Modifier
                        .clickable { onClearChat() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .testTag("clear_chat_button")
                )
            }
            HorizontalDivider(color = Color(0xFFE1E2EC), thickness = 1.dp)
        }
    }

    // Suggested Smart Recruiter Prompts
        val prepopulatedPrompts = listOf(
            "Tell me about your Ignis Sensor project",
            "What are your core technical skills?",
            "What are your background accomplishments?",
            "How can we get in contact with you?"
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(prepopulatedPrompts) { prompt ->
                SuggestionChip(
                    onClick = { onSendMessage(prompt) },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = Color.White,
                        labelColor = Color(0xFF44474E)
                    ),
                    border = BorderStroke(1.dp, Color(0xFFC4C6D0)),
                    shape = RoundedCornerShape(20.dp),
                    label = {
                        Text(
                            text = prompt,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    modifier = Modifier.testTag("suggestion_chip_${prompt.replace(" ", "_")}")
                )
            }
        }

        // Main Conversation List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 16.dp)
        ) {
            items(messages) { msg ->
                ChatBubbleRow(msg = msg)
            }

            if (isGenerating) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        AvatarBadge(isClone = true)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Card(
                                shape = chatBubbleShape(false),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White,
                                    contentColor = Color(0xFF44474E)
                                ),
                                border = BorderStroke(1.dp, Color(0xFFE1E2EC)),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        "Twin is contemplating...",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color(0xFF74777F)
                                    )
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(14.dp),
                                        strokeWidth = 2.dp,
                                        color = Color(0xFF005AC1)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "AI Assistant • typing",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = Color(0xFF74777F),
                                modifier = Modifier.padding(start = 2.dp)
                            )
                        }
                    }
                }
            }
        }

        // User Input Capsule & Footer Custom Signature
        Surface(
            modifier = Modifier.fillMaxWidth(),
            tonalElevation = 0.dp,
            color = Color.White
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                HorizontalDivider(color = Color(0xFFE1E2EC), thickness = 1.dp)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 16.dp)
                        .navigationBarsPadding(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(28.dp))
                            .background(Color(0xFFF3F4F9))
                            .border(1.dp, Color.Transparent, RoundedCornerShape(28.dp))
                            .padding(horizontal = 14.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BasicTextField(
                        value = inputMessage,
                        onValueChange = { inputMessage = it },
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 12.dp)
                            .testTag("chat_input_text_field"),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF1B1B1F)),
                        decorationBox = { innerTextField ->
                            if (inputMessage.isEmpty()) {
                                Text(
                                    "Ask about projects or experience...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF74777F)
                                )
                            }
                            innerTextField()
                        }
                    )
                    
                    if (inputMessage.isNotEmpty()) {
                        IconButton(
                            onClick = { inputMessage = "" },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear input",
                                tint = Color(0xFF74777F),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                    }

                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF005AC1))
                            .clickable {
                                if (inputMessage.isNotBlank()) {
                                    onSendMessage(inputMessage)
                                    inputMessage = ""
                                    keyboardController?.hide()
                                }
                            }
                            .testTag("send_chat_fab"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp).offset(x = 1.dp)
                        )
                    }
                }

                // Verified sub-brand signoff line
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "VERIFIED PORTFOLIO OF PRITHIVI RAJ SINGH",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    ),
                    color = Color(0xFF74777F),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}
}

@Composable
fun ChatBubbleRow(msg: ChatMessage) {
    val isUser = msg.sender == "USER"
    val timeString = remember(msg.timestamp) {
        try {
            val sdf = java.text.SimpleDateFormat("h:mm a", java.util.Locale.getDefault())
            sdf.format(java.util.Date(msg.timestamp))
        } catch (e: Exception) {
            "10:16 AM"
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        if (!isUser) {
            AvatarBadge(isClone = true)
            Spacer(modifier = Modifier.width(10.dp))
        }

        Column(
            modifier = Modifier.weight(1f, fill = false),
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
        ) {
            Card(
                shape = chatBubbleShape(isUser),
                colors = CardDefaults.cardColors(
                    containerColor = if (isUser) Color(0xFF005AC1) else Color.White,
                    contentColor = if (isUser) Color.White else Color(0xFF1B1B1F)
                ),
                border = if (isUser) null else BorderStroke(1.dp, Color(0xFFE1E2EC)),
                elevation = CardDefaults.cardElevation(defaultElevation = if (isUser) 2.dp else 1.dp)
            ) {
                Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                    Text(
                        text = msg.message,
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp)
                    )

                    // Inject View Case Study Action Item inside bubble if matching telemetry projects
                    if (!isUser && (msg.message.contains("Ignis Sensor", ignoreCase = true) ||
                                msg.message.contains("Agri-Grid", ignoreCase = true) ||
                                msg.message.contains("Synapse", ignoreCase = true))) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFF0F4FF))
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "View Tech Case Study / Code",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                ),
                                color = Color(0xFF005AC1)
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = Color(0xFF005AC1),
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = if (isUser) "Recruiter • $timeString" else "AI Assistant • $timeString",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = Color(0xFF74777F),
                modifier = Modifier.padding(start = 4.dp, end = 4.dp)
            )
        }

        if (isUser) {
            Spacer(modifier = Modifier.width(10.dp))
            AvatarBadge(isClone = false)
        }
    }
}

@Composable
fun AvatarBadge(isClone: Boolean) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(
                color = if (isClone) Color(0xFF005AC1) else Color(0xFF535F70)
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isClone) {
            Text(
                text = "AI",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelMedium
            )
        } else {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

fun chatBubbleShape(isUser: Boolean) = RoundedCornerShape(
    topStart = if (isUser) 16.dp else 2.dp,
    topEnd = if (isUser) 2.dp else 16.dp,
    bottomStart = 16.dp,
    bottomEnd = 16.dp
)

@Composable
fun TrainingCenterTab(
    name: String,
    title: String,
    email: String,
    bio: String,
    skills: String,
    personalityArchetype: String,
    customStyleInstructions: String,
    rawText: String,
    projects: List<Project>,
    achievements: List<Achievement>,
    onUpdateProfile: (String, String, String, String, String, String, String, String) -> Unit,
    onDeleteProject: (Int) -> Unit,
    onDeleteAchievement: (Int) -> Unit,
    onAddProjectClick: () -> Unit,
    onAddAchievementClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Current input buffers
    var nameInput by remember(name) { mutableStateOf(name) }
    var titleInput by remember(title) { mutableStateOf(title) }
    var emailInput by remember(email) { mutableStateOf(email) }
    var bioInput by remember(bio) { mutableStateOf(bio) }
    var skillsInput by remember(skills) { mutableStateOf(skills) }
    var rawInput by remember(rawText) { mutableStateOf(rawText) }
    var archetypeInput by remember(personalityArchetype) { mutableStateOf(personalityArchetype) }
    var customStyleInput by remember(customStyleInstructions) { mutableStateOf(customStyleInstructions) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section: Personal Profile Setup
        item {
            Text(
                text = "Personal Information",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = { Text("Name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("profile_name_input"),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = titleInput,
                        onValueChange = { titleInput = it },
                        label = { Text("Professional Title") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("profile_title_input"),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = emailInput,
                        onValueChange = { emailInput = it },
                        label = { Text("Contact Email / Link") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("profile_email_input"),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = bioInput,
                        onValueChange = { bioInput = it },
                        label = { Text("Executive Biography Summary") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("profile_bio_input"),
                        minLines = 3
                    )
                    OutlinedTextField(
                        value = skillsInput,
                        onValueChange = { skillsInput = it },
                        label = { Text("Key Technical Skills (Comma-separated)") },
                        placeholder = { Text("Kotlin, Compose, Android SDK, Python") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("profile_skills_input"),
                        singleLine = true
                    )
                    Button(
                        onClick = {
                            onUpdateProfile(
                                nameInput,
                                titleInput,
                                emailInput,
                                bioInput,
                                skillsInput,
                                rawInput,
                                archetypeInput,
                                customStyleInput
                            )
                        },
                        modifier = Modifier
                            .align(Alignment.End)
                            .testTag("save_profile_button")
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, sizeModifier(16))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Apply Core Profile Info")
                    }
                }
            }
        }

        // Section: Personality Alignment Customizer
        item {
            Text(
                text = "Personality & Code Brand Style",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Predefined Personality Archetypes",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Customize how your Digital Twin speaks with recruiters. Select a predefined archetype or define custom instructions below.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF44474E)
                    )

                    // Archetype select Row (Horizontal Chips Selection Row)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Formal", "Enthusiastic", "Concise", "Custom").forEach { arch ->
                            val isSelected = archetypeInput == arch
                            FilterChip(
                                selected = isSelected,
                                onClick = { archetypeInput = arch },
                                label = { Text(arch) },
                                modifier = Modifier.testTag("archetype_chip_$arch")
                            )
                        }
                    }

                    // Dynamic description of selected archetype style
                    val archetypeDescription = when (archetypeInput) {
                        "Formal" -> "Polite & Polished: Communicates using highly precise systems vocabulary, structured points, and extreme respect. Preferred for executive corporate screening."
                        "Enthusiastic" -> "Energetic & Passionate: Expresses high excitement, active phrasing, and extreme enthusiasm about engineering workshops and device technologies."
                        "Concise" -> "Direct & Punchy: Highly focused on saving time. Answers questions straight-to-the-point in 2-3 short bullet points, eliminating unnecessary filler."
                        else -> "Fully Customized: Instruct your clone exactly how they should structure, format, and voice their communications."
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF3F4F9), RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    ) {
                        Text(
                            text = archetypeDescription,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF1B1B1F)
                        )
                    }

                    // Custom instructions text field
                    OutlinedTextField(
                        value = customStyleInput,
                        onValueChange = { customStyleInput = it },
                        label = { Text("Custom Style Instructions") },
                        placeholder = { Text("e.g. Speak with lighthearted humor and mention your GDSC background where relevant.") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("profile_custom_style_input"),
                        minLines = 2
                    )

                    Button(
                        onClick = {
                            onUpdateProfile(
                                nameInput,
                                titleInput,
                                emailInput,
                                bioInput,
                                skillsInput,
                                rawInput,
                                archetypeInput,
                                customStyleInput
                            )
                        },
                        modifier = Modifier
                            .align(Alignment.End)
                            .testTag("apply_personality_button")
                    ) {
                        Icon(Icons.Default.Build, contentDescription = null, sizeModifier(16))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Apply Personality Alignment")
                    }
                }
            }
        }

        // Section: Sector Knowledge Base Explorer
        item {
            Text(
                text = "Sector Knowledge Base Explorer",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                var searchQuery by remember { mutableStateOf("") }
                val filteredItems = remember(searchQuery) {
                    if (searchQuery.isBlank()) {
                        com.example.data.KnowledgeBase.items
                    } else {
                        com.example.data.KnowledgeBase.items.filter {
                            it.title.contains(searchQuery, ignoreCase = true) ||
                                    it.category.contains(searchQuery, ignoreCase = true) ||
                                    it.content.contains(searchQuery, ignoreCase = true)
                        }
                    }
                }

                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Integrated Sector Brain & Q&A Cards",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "These smart articles are automatically matched and synthesized into the system prompt when recruiters ask relevant questions.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF44474E)
                    )

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Search Knowledge Base") },
                        placeholder = { Text("e.g. coroutines, recomposition, ble...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("knowledge_base_search_input"),
                        singleLine = true
                    )

                    // Scrollable/Compact list of articles in a Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 240.dp)
                            .background(Color(0xFFF3F4F9), RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    ) {
                        if (filteredItems.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No matching knowledge base cards found.", style = MaterialTheme.typography.bodySmall)
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(filteredItems) { kItem ->
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = Color.White),
                                        modifier = Modifier.fillMaxWidth(),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(8.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = kItem.title,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                                Box(
                                                    modifier = Modifier
                                                        .background(Color(0xFFE1E2EC), RoundedCornerShape(4.dp))
                                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                                ) {
                                                    Text(
                                                        text = kItem.category,
                                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                                        color = Color(0xFF1B1B1F)
                                                    )
                                                }
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = kItem.content,
                                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                                color = Color(0xFF1B1B1F)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section: Projects
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Project Showcase",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Button(
                    onClick = onAddProjectClick,
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
                    modifier = Modifier.testTag("add_project_button")
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add new project item", sizeModifier(14))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Add Project", style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        if (projects.isEmpty()) {
            item {
                Text(
                    text = "No projects loaded yet. Click Add Project above to load some!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        } else {
            items(projects) { project ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(project.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                            Text("Role: ${project.role}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(project.description, style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Tech Stack: ${project.technologies}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                            if (project.url.isNotBlank()) {
                                Text("Link: ${project.url}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                            }
                        }
                        IconButton(
                            onClick = { onDeleteProject(project.id) },
                            modifier = Modifier.testTag("delete_project_${project.id}")
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete project", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }

        // Section: Experiences & Achievements
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Experience & Achievements",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Button(
                    onClick = onAddAchievementClick,
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
                    modifier = Modifier.testTag("add_achievement_button")
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add experience block", sizeModifier(14))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Add Experience", style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        if (achievements.isEmpty()) {
            item {
                Text(
                    text = "No achievements or work details recorded. Click Add above!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        } else {
            items(achievements) { ach ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(ach.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                            Row {
                                Text(ach.issuerOrComp, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("•", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(ach.date, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(ach.description, style = MaterialTheme.typography.bodyMedium)
                        }
                        IconButton(
                            onClick = { onDeleteAchievement(ach.id) },
                            modifier = Modifier.testTag("delete_ach_${ach.id}")
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete experience record", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }

        // Section: Raw Training Prompts
        item {
            Text(
                text = "Extra Resume & Custom Instructions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Paste additional raw resume details or custom instructions here. These will feed directly into the clone's personality prompt constraint.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                    OutlinedTextField(
                        value = rawInput,
                        onValueChange = { rawInput = it },
                        label = { Text("Additional Custom Training Context") },
                        placeholder = { Text("E.g. Publications, extra workshops, GPA, soft skills...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("profile_raw_resume_input"),
                        minLines = 4
                    )
                    Button(
                        onClick = {
                            onUpdateProfile(
                                nameInput,
                                titleInput,
                                emailInput,
                                bioInput,
                                skillsInput,
                                rawInput,
                                archetypeInput,
                                customStyleInput
                            )
                        },
                        modifier = Modifier
                            .align(Alignment.End)
                            .testTag("save_raw_instructions_button")
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, sizeModifier(16))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Save Prompt Context")
                    }
                }
            }
        }
    }
}

@Composable
fun AddProjectDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var tech by remember { mutableStateOf("") }
    var link by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Project Detail") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Project Title") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("add_project_dialog_title")
                )
                OutlinedTextField(
                    value = role,
                    onValueChange = { role = it },
                    label = { Text("Your Role (e.g., Solo Developer)") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("add_project_dialog_role")
                )
                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Description") },
                    minLines = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("add_project_dialog_desc")
                )
                OutlinedTextField(
                    value = tech,
                    onValueChange = { tech = it },
                    label = { Text("Technologies (Comma-separated)") },
                    singleLine = true,
                    placeholder = { Text("Kotlin, Retrofit, Room") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("add_project_dialog_tech")
                )
                OutlinedTextField(
                    value = link,
                    onValueChange = { link = it },
                    label = { Text("Project URL (Optional)") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("add_project_dialog_link")
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank()) {
                        onConfirm(title, desc, role, tech, link)
                    }
                },
                modifier = Modifier.testTag("add_project_dialog_confirm")
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun AddAchievementDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var comp by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Achievement / Experience") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title / Role") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("add_ach_dialog_title")
                )
                OutlinedTextField(
                    value = comp,
                    onValueChange = { comp = it },
                    label = { Text("Company / Issuer / College") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("add_ach_dialog_comp")
                )
                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Date (e.g. Jan 2025 - Present)") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("add_ach_dialog_date")
                )
                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Details & Core Contributions") },
                    minLines = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("add_ach_dialog_desc")
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank()) {
                        onConfirm(title, comp, date, desc)
                    }
                },
                modifier = Modifier.testTag("add_ach_dialog_confirm")
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

fun sizeModifier(size: Int) = Modifier.size(size.dp)
