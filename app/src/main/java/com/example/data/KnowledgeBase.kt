package com.example.data

data class KnowledgeItem(
    val title: String,
    val category: String, // "Industry Trends", "Sector Companies", "Interview Q&As"
    val content: String,
    val keywords: List<String>
)

object KnowledgeBase {
    val items = listOf(
        // Industry Trends
        KnowledgeItem(
            title = "On-Device AI & Edge ML Sync",
            category = "Industry Trends",
            content = "The modern mobile industry is actively adopting on-device ML runtimes (such as TensorFlow Lite, PyTorch Mobile, and Gemini Nano under AICore). Running neural logic locally preserves biometric/privacy boundaries, operates completely offline, and eradicates server API token costs.",
            keywords = listOf("ai", "ml", "on-device", "edge", "tensorflow", "pytorch", "model", "neural", "gemini nano", "trend", "trends")
        ),
        KnowledgeItem(
            title = "Declarative UI & Jetpack Compose Multiplatform",
            category = "Industry Trends",
            content = "Engineering teams are rapidly refactoring traditional XML layouts to declarative Jetpack Compose interfaces. This unified, state-driven workflow reduces UI codebase size by 40% and simplifies shared rendering logic across iOS and desktop via Compose Multiplatform.",
            keywords = listOf("compose", "declarative", "multiplatform", "xml", "ui", "jetpack", "rendering", "graphics", "trend", "trends")
        ),
        KnowledgeItem(
            title = "Smart Wearables & BLE Declarative Telemetry",
            category = "Industry Trends",
            content = "Modern Wearables and Connected Devices use low-energy bluetooth telemetry services. Developers leverage structured Kotlin Coroutine Channels to batch and cache high-frequency health or telemetry packets onto a local Room DB, maintaining extreme battery efficiency.",
            keywords = listOf("ble", "bluetooth", "sensor", "telemetry", "iot", "peripheral", "packet", "background", "notification", "trend", "trends", "wearables", "ignis", "sensor")
        ),
        
        // Sector Companies
        KnowledgeItem(
            title = "Google Android Core Ecosystem",
            category = "Sector Companies",
            content = "Google drives the Android developer standard, championing Jetpack Compose, Kotlin Coroutines, and MVVM Architecture. They seek mobile advocates with sharp skills in Material 3 guidelines and native hardware service optimizations.",
            keywords = listOf("google", "android", "ecosystem", "material 3", "jetpack", "aicore", "company", "companies", "sector")
        ),
        KnowledgeItem(
            title = "Apple Mobile & CoreML Architecture Standards",
            category = "Sector Companies",
            content = "Apple dictates high-fidelity mobile standards using Swift and SwiftUI. Apple teams heavily emphasize on-device private processing, strict sandbox guidelines, and optimized pipeline structures for battery preservation.",
            keywords = listOf("apple", "ios", "swift", "neural engine", "silicon", "coreml", "company", "companies", "sector")
        ),
        KnowledgeItem(
            title = "Connected IoT Specialists & Health Startups",
            category = "Sector Companies",
            content = "Startups worldwide hire mobile engineers who can pair hardware BLE sensors (e.g. cardiac monitors, forestry trackers like Ignis sensor, or soil hydration adapters) with offline-first local SQL persistence shields.",
            keywords = listOf("startup", "device", "medical", "agricultural", "healthcare", "sensor-fusion", "forestry", "firm", "company", "companies", "sector")
        ),

        // Interview Q&As
        KnowledgeItem(
            title = "Kotlin Coroutines vs Threads Core",
            category = "Interview Q&As",
            content = "Question: What are Kotlin Coroutines and how do they differ from OS Threads?\nAnswer: OS Threads are heavy and managed by the kernel; creating or blocking them wastes CPU cycles and battery. Coroutines are cooperatively scheduled, lightweight tasks carrying multiplexed contexts. Thousands of coroutines can operate seamlessly on a small thread pool, using suspend functions to yield threads during slow database or network operations without blocking.",
            keywords = listOf("coroutine", "thread", "asynchronous", "suspend", "concurrency", "scheduler", "dispatcher", "flow", "interview", "question", "questions", "answer", "answers")
        ),
        KnowledgeItem(
            title = "Declarative State, Recomposition & UI Performance",
            category = "Interview Q&As",
            content = "Question: What is recomposition in Jetpack Compose, and how do you prevent performance sluggishness?\nAnswer: Recomposition is the re-execution of Composable functions when their state inputs change. To keep rendering speeds well within 16ms (60/120 FPS), developers use: 1) 'remember { mutableStateOf(val) }' to cache state locally. 2) 'derivedStateOf' to debounce rapid UI triggers like scrolling. 3) Declaring stable parameters to avoid unnecessary recomposition passes of heavy list items.",
            keywords = listOf("recomposition", "remember", "derivedstateof", "state", "optimize", "perf", "performance", "rendering", "lazylist", "interview", "question", "questions", "answer", "answers")
        ),
        KnowledgeItem(
            title = "Offline-First Mobile Architecture Principle",
            category = "Interview Q&As",
            content = "Question: Explain how to design a sustainable offline-first application model.\nAnswer: offline-first architectures treat a local relational database (Room DB / SQLite) as the absolute single source of truth for UI data. The UI binds directly to a persistent database Flow. When REST network requests complete (such as via Retrofit), they write directly into the database. The database then broadcasts the updated state to the UI flow, which guarantees responsiveness under broken network conditions.",
            keywords = listOf("offline", "offline-first", "room", "retrofit", "flow", "truth", "network", "cache", "sqlite", "interview", "question", "questions", "answer", "answers")
        ),
        KnowledgeItem(
            title = "BLE Telemetry Connection & Power Conservation",
            category = "Interview Q&As",
            content = "Question: How do you implement robust Bluetooth BLE packet consumption with low power impact?\nAnswer: Reliable BLE connections utilize distinct GATT state machines. To minimize battery drainage on the radio, you should: 1) batch-write telemetry packets (e.g. storing sensor notifications in memory and writing them block-by-block to SQLite every 5 seconds). This minimizes CPU wake locks, cutting radio draw by up to 22% compared to writing packets individual-by-individual.",
            keywords = listOf("ble", "battery", "conservation", "optimize", "power", "packet", "scans", "connection", "gatt", "interview", "question", "questions", "answer", "answers")
        )
    )

    fun retrieveRelevant(query: String): List<KnowledgeItem> {
        val lowercaseQuery = query.lowercase()
        return items.filter { item ->
            item.keywords.any { keyword -> lowercaseQuery.contains(keyword) }
        }
    }
}
