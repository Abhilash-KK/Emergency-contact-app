# Emergency Contact App

An Android application built with Java and XML that gives users instant access to essential emergency helplines, a one-tap **SOS Panic Button** that shares live GPS coordinates via SMS, nearby emergency facility lookup on Google Maps, and local management of personal family emergency contacts.

---

## Features

- **Animated Splash Screen (`SplashActivity`)**
  - Smooth fade-in intro screen featuring the app branding and a **Get Started** button to enter the main dashboard.
- **One-Tap SOS Panic Button (`MainActivity`)**
  - Continuous pulsing animation (`res/anim/pulse.xml`) for immediate visibility.
  - Requests runtime `SEND_SMS` and `ACCESS_FINE_LOCATION` permissions when needed.
  - Fetches high-accuracy live GPS coordinates using Google Play Services `FusedLocationProviderClient`.
  - Automatically sends an emergency SMS containing a Google Maps location link (`https://maps.google.com/?q=lat,lng`) to all saved family contacts.
- **Quick-Access Emergency Services**
  - Pre-configured buttons for **Police (112)**, **Ambulance (108)**, **Doctor (9876543210)**, **Fire Force (101)**, **Women Helpline (1091)**, **Child Helpline (1098)**, and **Cyber Crime Helpline (1930)**.
- **Service Action Center (`ServiceDetailActivity`)**
  - **Call Now**: Opens the phone dialer (`ACTION_DIAL`) pre-filled with the service number.
  - **Send SMS Message**: Opens the default messaging app (`ACTION_SENDTO`) with a pre-filled emergency assistance message.
  - **Find Nearby on Maps**: Launches Google Maps (with fallback to other installed map apps) to search for nearby Police Stations, Hospitals, or Fire Stations.
- **Family Contacts Manager (SQLite CRUD)**
  - Stores emergency family contacts locally in an SQLite database (`contacts_db`).
  - Displays saved contacts in a `RecyclerView` with `CardView` items.
  - **Add & Edit** contacts via a Material Design `BottomSheetDialog`.
  - **One-tap Call** when selecting a contact card, and **Delete** support directly from the list.
- **Light & Dark Mode Support**
  - Built on `Theme.Material3.DayNight.NoActionBar` with edge-to-edge layout handling and adaptive theme colors (`values` and `values-night`).

---

## Technology Stack

- **Language:** Java (Java 11 source/target compatibility)
- **Platform:** Android SDK (`minSdk` 28 / Android 9.0, `compileSdk` & `targetSdk` 36)
- **Build System:** Gradle `9.2.1` (Kotlin DSL) & Android Gradle Plugin `9.0.1`
- **UI & AndroidX Libraries:**
  - `androidx.appcompat:appcompat` (`1.7.1`)
  - `com.google.android.material:material` (`1.13.0` — Material 3 Theme & `BottomSheetDialog`)
  - `androidx.activity:activity` (`1.13.0` — `EdgeToEdge`)
  - `androidx.constraintlayout:constraintlayout` (`2.2.1`)
  - `RecyclerView` & `CardView`
  - Android XML Scale Animation & `ViewPropertyAnimator`
- **Location & Communication:**
  - Google Play Services Location (`com.google.android.gms:play-services-location:21.0.1`)
  - Android `SmsManager` & Implicit Intents (`ACTION_DIAL`, `ACTION_SENDTO`, `ACTION_VIEW`)
- **Database:** SQLite (`SQLiteOpenHelper`)
- **Testing:** JUnit (`4.13.2`), AndroidX Test JUnit (`1.3.0`), Espresso (`3.7.0`)

---

## Installation & Setup

### Prerequisites

- **Android Studio** (with Android SDK API 36 installed)
- **JDK 11+** (configured with Gradle Java 21 daemon toolchain)
- An **Android device** running Android 9.0 (API 28) or higher (recommended for testing live GPS and SMS features) or an Android Emulator with Google Play Services

### Steps to Run Locally

1. **Clone the repository**
   ```bash
   git clone https://github.com/Abhilash-KK/Emergency-contact-app.git
   cd Emergency-contact-app
   ```

2. **Open in Android Studio**
   - Launch Android Studio and select **Open**.
   - Choose the cloned project folder and let Gradle sync dependencies.

3. **Build via Command Line (Optional)**
   - **macOS / Linux:**
     ```bash
     ./gradlew assembleDebug
     ```
   - **Windows (PowerShell / CMD):**
     ```powershell
     .\gradlew.bat assembleDebug
     ```

4. **Run the App**
   - Connect your Android phone via USB (with **USB Debugging** enabled) or start an emulator.
   - Click **Run** in Android Studio, or install from the terminal:
     ```bash
     ./gradlew installDebug
     ```
   - When prompted on the device, grant **Location** and **SMS** permissions so the SOS Panic Button can retrieve coordinates and send emergency messages.

---

## Project Structure

```text
Emergency-contact-app/
├── app/
│   ├── build.gradle.kts                          # App module config & dependencies
│   └── src/main/
│       ├── AndroidManifest.xml                   # Permissions (SMS, Location) & Activities
│       ├── java/com/example/emergencycontactapp/
│       │   ├── SplashActivity.java               # Launcher screen with fade-in animation
│       │   ├── MainActivity.java                 # SOS alert logic, services, contact CRUD
│       │   ├── ServiceDetailActivity.java        # Call, SMS, and Maps lookup per service
│       │   ├── Contact.java                      # Contact data model (id, name, phone)
│       │   ├── ContactAdapter.java               # RecyclerView adapter for family contacts
│       │   └── DbHelper.java                     # SQLiteOpenHelper for contacts_db
│       └── res/
│           ├── anim/pulse.xml                    # Pulsing scale animation for SOS button
│           ├── drawable/                         # App background & launcher vector assets
│           ├── layout/
│           │   ├── activity_splash.xml           # Splash screen UI
│           │   ├── activity_main.xml             # Main dashboard UI
│           │   ├── activity_service_detail.xml   # Service detail actions UI
│           │   ├── dialog_add_contact.xml        # BottomSheet form for Add/Edit contact
│           │   └── item_contact.xml              # CardView layout for a contact row
│           ├── values/                           # Light theme, colors, and app strings
│           └── values-night/                     # Night theme configuration
├── gradle/
│   ├── libs.versions.toml                        # Dependency version catalog
│   └── wrapper/                                  # Gradle wrapper files
├── build.gradle.kts                              # Top-level Gradle build file
└── settings.gradle.kts                           # Gradle project settings
```

---

## Live Website

> This is a native Android mobile application designed to run directly on Android devices; it is not hosted as a web application.

---

## Author

- **Abhilash**
  - GitHub: [@Abhilash-KK](https://github.com/Abhilash-KK)
  - Email: kkabhilash30@gmail.com
