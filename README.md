# Comments List Project

An android application built with Kotlin and Jetpack Compose using MVVM/MVI architecture.

## Steps to run the app
### Android
1. Open the July-2025 project in Android Studio
2. Select an emulator or connected device
3. Run the `app` module
4. **Alternative**: Install the app using the Gradle command:
   ```bash
   ./gradlew installDebug
   ```

## Focus Areas
- **MVVM architecture** leveraging Kotlin's coroutines and StateFlow to handle UI state management, and using a repository pattern for clean data management
- **Scalable foundation** - Although this application is a single-screen proof-of-concept app, I added navigation and dependency injection for easy scalability and future feature expansion

### User Experience
- **Professional UI**: Clean, Material 3-based interface with proper loading, refreshing, and error states
- **Handle name & email rendering, UI Requirements** : This implementation is handled differently in portrait and landscape orientations to meet requirements. In landscape mode, the additional width provides a better visual layout and prevents text overlap, or multiline text wrap.

### Suggestion on current UI Design
- In the current design, the name and email ID are placed in the same row but different columns, with a requirement to display both without truncation. This forces both texts to wrap into multiple lines, which affects readability. I recommend placing the name and email in the same column to allow cleaner multiline rendering and better visual alignment.

### Screenshots and Videos

| Comments List, Imagee picker & Refresh | Error case | Orientation Change |
|----------------------------------------| ------- |------- |
| main screen  - video                       | [error-message-handling.webm](https://github.com/user-attachments/assets/29f4337e-65f5-46a7-bb13-0fbe63855a8e)    | [orientation-change.webm](https://github.com/user-attachments/assets/019b9cfd-7948-49e0-9462-b9c1dade40bb)   |
| main screen - photo                    | <img width="376" height="789" alt="image" src="https://github.com/user-attachments/assets/f5f52d45-6d70-4b6d-8e83-83c4ac6be31a" /> | <img width="789" height="376" alt="image" src="https://github.com/user-attachments/assets/0b2c009d-6427-47be-9b77-197bd38dbe99" />|












