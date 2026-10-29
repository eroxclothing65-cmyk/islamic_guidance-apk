# Islamic Guidance (ইসলামিক গাইডেন্স)
> **Your Daily Companion to Islam — সম্পূর্ণ বাংলা ও ইংরেজি ইসলামিক অ্যান্ড্রয়েড অ্যাপ**

---

## 📌 Overview

**Islamic Guidance** is a modern, authentic native Android application built with **Kotlin**, **Jetpack Compose**, **Material 3**, **Room Database**, and **DataStore Preferences**. It is designed specifically for Bengali (বাংলা) and English-speaking Muslims to provide verified Quranic scriptures, authentic Sahih Hadiths, Hisn al-Muslim daily Azkar, essential Duas, digital Tasbih counter, prayer times schedule, and a live Qibla compass.

---

## 📱 App Highlights & Features

1. **Home Dashboard**
   - Assalamu Alaikum greeting in Arabic & Bengali.
   - Dual Calendar: Hijri (হিজরি) and Gregorian (ঈসায়ী) dates with accurate calculation.
   - Next prayer countdown badge with remaining hours and minutes.
   - Horizontal prayer timetable (Fajr, Sunrise, Dhuhr, Asr, Maghrib, Isha).
   - Quick access cards for Holy Quran, Hadith, Azkar, Duas, Tasbih, and Qibla.
   - Featured Daily Quran Verse with audio recitation.
   - Featured Daily Hadith from Sahih al-Bukhari with verified grade.
   - Daily Worship Goals tracker with interactive checkboxes.
   - Continue Reading jump-back card.

2. **Holy Quran (আল-কুরআন)**
   - All 114 Surahs indexed with Arabic names, Bengali transliterated names, English names & meanings, verse counts, and Makki/Madani classification.
   - Complete Surah verse-by-verse reader with original Arabic text, authoritative Bengali translation (Islamic Foundation / Maulana Muhiuddin Khan), and English translation.
   - Live audio recitation streaming player (Mishary Rashid Alafasy) with Play, Pause, Repeat, and Stop in a floating player bar.
   - Dynamic font-size adjustment sliders for Arabic (22sp–40sp) and Bengali (14sp–26sp).
   - Instant verse bookmarking, copying to clipboard, and sharing via Android Share Sheet.

3. **Authentic Hadith Library (সহীহ হাদিস)**
   - Collections from **Sahih al-Bukhari** (সহীহ বুখারী) and **Sahih Muslim** (সহীহ মুসলিম).
   - Categorized by topic: Faith & Belief, Prayer, Fasting & Ramadan, Charity & Zakat, Good Character, Family, Knowledge, and Daily Etiquettes.
   - Displays original Arabic text, Bengali translation, English translation, reference number, chapter name, narrator attribution, and verified authenticity grade.

4. **Hisn al-Muslim Daily Azkar (হিসনুল মুসলিম)**
   - Supplications categorized: Morning Azkar, Evening Azkar, After-Prayer Azkar, Before Sleeping, After Waking, Home Entry/Exit, Mosque Entry/Exit, Istighfar, and Salawat.
   - Includes Arabic text, Bengali pronunciation/transliteration, Bengali meaning, English meaning, and recommended repetition count.
   - Interactive tap-to-count counter button with vibration and daily completion checkmarks.

5. **Digital Tasbih (ডিজিটাল তাসবিহ)**
   - Large circular touch button with golden animated progress ring.
   - Presets: SubhanAllah, Alhamdulillah, Allahu Akbar, La ilaha illallah, Astaghfirullah, Salawat.
   - Target presets: 33, 100, 1000, and custom targets.
   - Undo last tap button and reset with confirmation dialog.
   - Haptic vibration feedback toggle.

6. **Authentic Duas (কুরআন ও সুন্নাহর দোয়া)**
   - Categorized Quranic *Rabbana* Duas and Prophetic Masnoon Duas for Protection, Forgiveness, Parents, Relief from Anxiety & Debt, Health, and Knowledge.
   - Detailed Arabic text, pronunciation, meaning, and source citations.

7. **Prayer Times & Live Qibla Compass (নামাজ ও কিবলা)**
   - Accurate solar calculation for Fajr, Sunrise, Dhuhr, Asr (Hanafi standard), Maghrib, and Isha.
   - City selector for major cities in Bangladesh (Dhaka, Chittagong, Sylhet, Khulna, Rajshahi, Barisal, Rangpur, Mymensingh) and international locations (Mecca, Medina, London, New York, Dubai).
   - Real-time Qibla compass utilizing device rotation vector and magnetic field sensors pointing toward the Holy Kaaba (21.4225° N, 39.8262° E) with alignment detection.

8. **Bilingual & Theming (বাংলা / English, Light / Dark)**
   - Default language: Bengali (বাংলা), switchable to English.
   - Material 3 dynamic theming: Light Mode (emerald green & warm cream), Dark Mode (deep charcoal & emerald green), and System Default.
   - Saved persistently across restarts.

9. **Offline Functionality & Storage Architecture**
   - **Room Database**: Local SQLite storage for bookmarks, reading history position, and daily worship logs.
   - **DataStore**: Modern asynchronous preferences for language, theme, font sizes, vibration, sound, and selected city.

---

## 🛠️ Project Structure & Architecture

```text
app/src/main/
├── AndroidManifest.xml
├── java/com/
│   ├── example/
│   │   ├── MainActivity.kt               # Navigation router & main activity
│   │   ├── ui/theme/                     # Theme, Color (emerald/gold), Type
│   │   └── islamicguidance/
│   │       ├── data/
│   │       │   ├── Models.kt             # Domain entities (Surah, Hadith, Azkar, Dua)
│   │       │   ├── Localization.kt       # Complete Bengali & English translations
│   │       │   ├── QuranData.kt          # 114 Surahs dataset and verified verses
│   │       │   ├── HadithData.kt         # Sahih Bukhari & Muslim authentic hadiths
│   │       │   ├── AzkarData.kt          # Hisn al-Muslim supplications dataset
│   │       │   ├── DuaData.kt            # Rabbana & Masnoon Duas dataset
│   │       │   ├── PrayerCalculator.kt   # Solar prayer equations & Qibla math
│   │       │   ├── UserPreferencesRepository.kt
│   │       │   └── local/
│   │       │       ├── RoomDatabase.kt   # Room Entity, DAO, and Database
│   │       │       └── IslamicDataStore.kt # DataStore preferences manager
│   │       ├── service/
│   │       │   ├── QuranAudioPlayer.kt   # Streaming Quran recitation player
│   │       │   └── CompassSensorManager.kt # Hardware sensor manager for Qibla
│   │       └── ui/
│   │           ├── IslamicViewModel.kt   # Central state management
│   │           ├── components/           # TopBar, BottomNav, AudioPlayerBar
│   │           └── screens/
│   │               ├── HomeScreen.kt
│   │               ├── QuranScreens.kt   # Surah list & Verse reader
│   │               ├── HadithScreen.kt
│   │               ├── AzkarScreen.kt
│   │               ├── TasbihScreen.kt
│   │               ├── DuasScreen.kt
│   │               ├── PrayerAndQiblaScreens.kt
│   │               └── SettingsAndAuxScreens.kt # Settings, Bookmarks, Search
└── res/
    ├── drawable/                         # Adaptive launcher icons & assets
    ├── mipmap-*/                         # App launcher icon densities (mdpi-xxxhdpi)
    └── values/                           # Strings, colors, themes
```

---

## 📦 Requirements

- **Android Studio**: Android Studio Ladybug (2024.2.1+) or newer
- **JDK**: Java 17 or Java 21 (Temurin or Corretto recommended)
- **Android SDK**:
  - `compileSdk`: **36**
  - `targetSdk`: **36**
  - `minSdk`: **24** (Android 7.0 Nougat and above)
- **Gradle**: 9.1+ with Kotlin 2.2+

---

## 🚀 Step-by-Step Guide: Open, Build, and Generate APK

### Step 1: Open the Project in Android Studio
1. Launch **Android Studio**.
2. Click **Open** (or **File > Open**).
3. Navigate to and select the root directory of this repository (`Islamic-Guidance/`).
4. Click **OK**.

### Step 2: Install the Required Android SDK
1. In Android Studio, go to **Tools > SDK Manager** (or Settings > Appearance & Behavior > System Settings > Android SDK).
2. Under the **SDK Platforms** tab, ensure **Android API 36** (or your preferred compatible SDK) is installed.
3. Under the **SDK Tools** tab, ensure the following are installed:
   - Android SDK Build-Tools
   - Android SDK Command-line Tools
   - Android Emulator (if testing on virtual device)

### Step 3: Sync Gradle
1. When prompted by Android Studio, click **Sync Project with Gradle Files** (the elephant icon with blue arrow in the top right toolbar).
2. Alternatively, go to **File > Sync Project with Gradle Files**.
3. Wait for the dependencies to download and index.

### Step 4: Build and Run the App
1. Connect an Android smartphone via USB with USB Debugging enabled, or start an Android Virtual Device (AVD) from Device Manager.
2. Select **app** in the run configurations dropdown.
3. Click the green **Run** button (`Shift + F10`).
4. The application will compile, install, and launch on your device.

---

## 🔨 How to Generate Installable APKs

### Method A: Generate Debug APK (Fastest, No Keystore Required)

#### Option 1: Via Android Studio GUI
1. In the top menu, go to **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
2. Android Studio will compile the application.
3. Once completed, a pop-up in the bottom-right corner will notify: `APK(s) generated successfully for 1 module: 'app'`.
4. Click **locate** to open the folder containing `app-debug.apk`.
   - File path: `app/build/outputs/apk/debug/app-debug.apk`

#### Option 2: Via Terminal / Command Line
Open the terminal in the root directory and execute:
```bash
./gradlew assembleDebug
```
(On Windows Command Prompt: `gradlew.bat assembleDebug`)

The compiled debug APK will be created at:
```text
app/build/outputs/apk/debug/app-debug.apk
```
You can transfer this APK directly to your Android device via USB, email, WhatsApp, or Google Drive, and install it.

---

### Method B: Generate Signed Release APK (For Production / Distribution)

#### Option 1: Via Android Studio GUI
1. Go to **Build > Generate Signed Bundle / APK...**.
2. Select **APK** and click **Next**.
3. **Key store path**:
   - If you have an existing keystore, click **Choose existing...**.
   - To create a new one, click **Create new...**, choose a destination (e.g. `release-key.jks`), set passwords, and fill out certificate details.
4. Enter your **Key store password**, **Key alias**, and **Key password**.
5. Click **Next**.
6. Select the **release** build variant.
7. Click **Create** / **Finish**.
8. Android Studio will generate your signed release APK at:
   ```text
   app/build/outputs/apk/release/app-release.apk
   ```

#### Option 2: Via GitHub Actions Automated Workflow
This repository includes a pre-configured release workflow at `/.github/workflows/release.yml`.
When you push a tag (e.g., `git tag v1.0.0 && git push origin v1.0.0`) or trigger it manually from the **Actions** tab on GitHub:
1. It builds the release APK.
2. It publishes the release with a directly downloadable asset named `Islamic-Guidance-v1.0.0.apk`.
3. If the release step encounters an issue, the Actions artifact remains permanently downloadable from the GitHub Actions run summary page.

---

## 📄 License & Attribution

- **Holy Quran Text**: Tanzil Project & King Fahd Quran Complex (Uthmani Script).
- **Bengali Quran Translation**: Islamic Foundation Bangladesh & Maulana Muhiuddin Khan.
- **English Translation**: Saheeh International.
- **Hadith Collections**: Sahih al-Bukhari & Sahih Muslim.
- **Azkar & Duas**: Hisn al-Muslim (Fortress of the Muslim) by Dr. Sa'id ibn Ali al-Qahtani.
