# Punch-In Tracker

An Android application built with Jetpack Compose for tracking location-based punch-ins with automatic location recording and route visualization.

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple.svg)](https://kotlinlang.org)

**Author:** Ravish Mishra  
**GitHub:** [@ravishmishralko](https://github.com/ravishmishralko)  
**Date:** 30 November 2025

---

## 📱 Features

### Core Functionality
- **User Authentication** - Secure login/logout system with persistent sessions
- **Automated Location Tracking** - Records GPS coordinates every 10 minutes
- **Smart Notifications** - Warning alerts 1 minute before next punch-in
- **Route Visualization** - Interactive Google Maps display of all tracked locations
- **Screen Lock Protection** - All screens freeze during tracking except Punch-In screen
- **Local Data Storage** - Offline-capable with Room Database

### Technical Highlights
- ✨ Modern UI with Jetpack Compose
- 🎨 Material Design 3 components
- 🗺️ Google Maps SDK integration
- 💾 Room Database for persistence
- 🔄 Kotlin Coroutines & Flow
- 📊 MVVM architecture pattern
- 🔐 DataStore for preferences

---

## 🛠️ Tech Stack

| Component | Technology |
|-----------|-----------|
| **UI Framework** | Jetpack Compose |
| **Language** | Kotlin |
| **Architecture** | MVVM + Repository Pattern |
| **Database** | Room |
| **Maps** | Google Maps SDK for Android |
| **Async** | Kotlin Coroutines + Flow |
| **Dependency Injection** | Manual DI |
| **Storage** | DataStore Preferences |
| **Permissions** | Accompanist Permissions |

---

## 📋 Requirements

- **Android Studio:** Hedgehog (2023.1.1) or later
- **Minimum SDK:** API 24 (Android 7.0)
- **Target SDK:** API 36
- **Compile SDK:** API 36
- **Kotlin Version:** 2.1.0
- **Gradle Version:** 8.7.3

---

## 🚀 Setup Instructions

### 1. Clone the Repository
```bash
git clone https://github.com/ravishmishralko/punchintracker.git
cd punchintracker
```

### 2. Get Google Maps API Key

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select an existing one
3. Enable **Maps SDK for Android**
4. Navigate to **Credentials** → **Create Credentials** → **API Key**
5. Copy your API key

### 3. Add API Key to Project

Open `app/src/main/AndroidManifest.xml` and replace `YOUR_API_KEY_HERE`:
```xml
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="YOUR_ACTUAL_API_KEY_HERE"/>
```

### 4. Build and Run

1. Open the project in Android Studio
2. Wait for Gradle sync to complete
3. Connect an Android device or start an emulator
4. Click **Run** (Shift + F10)

---

## 📱 How to Use

### Login
1. Enter any username (will be created automatically)
2. Enter password (minimum 4 characters)
3. Click **Login** or **Create Account**

### Start Tracking
1. From Home screen, tap **Start Punch-In**
2. Grant location permissions when prompted
3. Tap **Start Tracking**
4. The app will record your location immediately and then every 10 minutes
5. A warning appears 1 minute before each punch-in
6. During tracking, other screens are locked for data integrity

### View Route
1. From Home screen, tap **View Route**
2. See all your punch-in locations on Google Maps
3. Markers show individual punch-ins
4. Blue line connects all locations in sequence
5. Tap markers to see timestamp details

### Logout
1. From Home screen, tap **Logout** button
2. Your session will be cleared

---

## 📁 Project Structure
```
app/src/main/java/com/office/punchintracker/
├── data/
│   ├── local/
│   │   ├── PunchInDatabase.kt      # Room database setup
│   │   ├── PunchInDao.kt           # Database access object
│   │   └── PunchInEntity.kt        # Database entity model
│   └── repository/
│       └── PunchInRepository.kt    # Data repository layer
├── ui/
│   ├── screens/
│   │   ├── LoginScreen.kt          # Authentication screen
│   │   ├── HomeScreen.kt           # Main dashboard
│   │   ├── PunchInScreen.kt        # Location tracking screen
│   │   └── RouteScreen.kt          # Map visualization screen
│   ├── navigation/
│   │   └── NavGraph.kt             # Navigation setup
│   └── theme/
│       └── Theme.kt                # Material theme configuration
└── MainActivity.kt                  # Application entry point
```

---

## 🔑 Key Dependencies
```kotlin
// Jetpack Compose
androidx.compose.ui:ui
androidx.compose.material3:material3

// Navigation
androidx.navigation:navigation-compose:2.8.4

// Room Database
androidx.room:room-runtime:2.6.1
androidx.room:room-ktx:2.6.1

// Google Maps
com.google.maps.android:maps-compose:4.3.3
com.google.android.gms:play-services-maps:19.0.0
com.google.android.gms:play-services-location:21.3.0

// Permissions
com.google.accompanist:accompanist-permissions:0.36.0

// DataStore
androidx.datastore:datastore-preferences:1.1.1
```

---

## 🎯 Features Breakdown

### 1. Login/Logout Screen
- Simple authentication with username/password
- Account creation functionality
- Persistent session management using DataStore
- Input validation

### 2. Home Screen
- Overlay layout design
- Two main action cards (Start Punch-In, View Route)
- Welcome message with username
- Logout functionality
- Clean, intuitive navigation

### 3. Punch-In Screen
- Real-time countdown timer (10-minute intervals)
- Location permission handling
- Animated pulsing indicator during tracking
- Warning notification (1 minute before punch-in)
- Display of last recorded location coordinates
- Start/Stop tracking controls
- Screen remains active while other screens are locked

### 4. Route Display Screen
- Google Maps integration
- Markers for each punch-in location
- Polyline connecting all locations
- Tap markers to view timestamps
- Route summary with total points
- Clear history option
- Empty state handling

---

## 🔒 Permissions Required
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```

---

## 🧪 Testing

### Manual Testing Steps

1. **Authentication Flow**
   - Test login with valid/invalid credentials
   - Verify session persistence after app restart
   - Test logout functionality

2. **Location Tracking**
   - Start tracking and verify immediate location capture
   - Wait for 10-minute interval
   - Verify warning appears at 9-minute mark
   - Check database entries

3. **Route Visualization**
   - Verify all punch-ins appear as markers
   - Check polyline connects points correctly
   - Test marker tap functionality
   - Verify empty state when no data

4. **Screen Lock**
   - Start tracking
   - Try navigating to other screens
   - Verify only Punch-In screen is accessible

---

## 🏗️ Build APK

### Debug APK
```bash
./gradlew assembleDebug
```
Output: `app/build/outputs/apk/debug/app-debug.apk`

### Release APK
```bash
./gradlew assembleRelease
```
Output: `app/build/outputs/apk/release/app-release.apk`

---

## 🐛 Known Limitations

- Requires Google Maps API key (not included in repository for security)
- Location tracking accuracy depends on device GPS capability
- Background location tracking requires additional implementation for production use
- 10-minute interval is fixed (could be made configurable)

---

## 🔮 Future Enhancements

- [ ] Background service for tracking even when app is closed
- [ ] Configurable punch-in intervals
- [ ] Export route data (CSV/JSON)
- [ ] Multiple user profiles
- [ ] Date range filtering for routes
- [ ] Distance calculation between punch-ins
- [ ] Dark mode toggle
- [ ] Biometric authentication

---

## 📄 License
```
MIT License

Copyright (c) 2025 Ravish Mishra

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

## 👤 Author

**Ravish Mishra**

- GitHub: [@ravishmishralko](https://github.com/ravishmishralko)
- Email: ravishmishralko@gmail.com

---

## 🙏 Acknowledgments

- Android Jetpack Compose documentation
- Google Maps Platform
- Material Design 3 guidelines
- Android developer community

---

## 📞 Support

For any questions or issues, please open an issue on GitHub or contact me directly.

---

**Note:** This project was developed as part of a technical assignment to demonstrate Android development skills using modern technologies and best practices.
```

---

## For the APK Attachment Issue

The APK file is likely too large for email. Here are solutions:

### Solution 1: Upload to Google Drive (Recommended)

1. **Upload APK to Google Drive:**
   - Go to [drive.google.com](https://drive.google.com)
   - Click **New** → **File Upload**
   - Select your `app-debug.apk` file
   - Wait for upload to complete

2. **Get Shareable Link:**
   - Right-click on the uploaded file
   - Click **Share**
   - Change to **"Anyone with the link"**
   - Click **Copy link**

3. **Update Email:**
```
**APK File:**  
The debug APK is available for download here:  
https://drive.google.com/file/d/1StnXmSYZLJxOiaTKUiYl8sTSYe2wA2xG/view?usp=drive_link

File Name: PunchInTracker-debug.apk  
File Size: [XX MB]  
Minimum Android Version: 7.0 (API 24)
