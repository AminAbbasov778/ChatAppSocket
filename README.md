💬 ChatApp (Real-Time WebSocket Chat + Clean Architecture + MVI + Hilt)

ChatApp is a modern Android chat application built with Kotlin, designed to deliver real-time messaging with rich UI features, typing indicators, message grouping, and user presence updates.

The app is built using MVI (Model-View-Intent) architecture, Clean Architecture, Kotlin Coroutines, Jetpack Compose, and Hilt for dependency injection. Navigation is type-safe using Jetpack Navigation.

📸 Screenshots & Demo
| Home Screen | Chat Screen |
|-------------|------------|
| <img src="screenshots/home.png" width="200" height="400"> | <img src="screenshots/chat.png" width="200" height="400"> |

📱 Key Screens & Features

### Home Screen
- Displays a list of all users
  - Online/offline status indicators
  - Last message preview
  - Unread message count badge
- Search bar with instant filtering using Flow
- Clickable user items navigate to Chat Screen
- Responsive and adaptive UI built with Jetpack Compose

### Chat Screen
- Real-time messaging via WebSocket (Socket.IO)
- Supports sending and receiving messages instantly
- Message grouping (start/end of block) with custom shapes
- Shows sender/receiver profile images dynamically
- Message timestamp appears after a delay (10s) for each message
- Scrolls automatically to the newest message
- Keyboard handling with imePadding and LazyColumn scroll

🧩 Architecture & Flow

**MVI (Model-View-Intent):**
- State → immutable UI state (e.g., ChatState, HomeState)
- Event / Intent → user actions or system events (e.g., ChatEvent, HomeEvent)
- ViewModel → interprets events, updates state, and triggers side effects (e.g., sending messages)

**Clean Architecture:**
- Domain → UseCases for chat and home logic
- Data → Repositories + Socket handler for real-time communication
- Presentation → Jetpack Compose UI + state handling

**Socket.IO Integration:**
- Handles `user_list`, `user_online`, `user_offline`, `receive_message` events
- Sends messages with `send_message` event
- Maintains local last message info per user
- Supports entering chat rooms per user

**State Handling:**
- `ChatState`: messages, sender info, current input text
- `HomeState`: user list, search text, filtered users, unread messages
- `MutableStateFlow` for reactive UI updates
- Delayed timestamp display for better UX

⚡ Real-Time Features

**Instant Messaging:**
- Messages are sent and received via WebSocket instantly
- UI updates in real-time with state flows

**User Presence:**
- Online/offline detection
- Shows live status updates

**Typing and Read Indicators:**
- Last message timestamp visible after a delay
- Unread message counts tracked per user

🛠 Technologies Used
| Technology | Purpose |
|------------|---------|
| Kotlin | Core programming language |
| Kotlin Coroutines | Asynchronous real-time data flow |
| Jetpack Compose | UI framework |
| Hilt | Dependency injection |
| Socket.IO | Real-time communication |
| Clean Architecture | Layered, maintainable structure |
| MVI | Predictable state management |


🔗 Navigation

**Home Screen → Chat Screen:**
- Uses type-safe route: `user_detail/{userId}`
- Passes user ID as argument to load chat state
- Back navigation handled via `navController.popBackStack()`

📝 Notes
- Messages group dynamically with rounded corners
- Keyboard appearance is handled to auto-scroll messages
- Supports online/offline state per user
- Search bar filters users in real-time using Flow and StateFlow
- **Note:** The server is **local only**. Other users are simulated; there is **no real external server** involved. This allows testing all features offline.

