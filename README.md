# Rick & Morty App - Arizona

## <a name="introduction"></a> Introduction :

A modern Android application built as a submission for the **BIGIO Mobile Developer Take Home Challenge**. This app leverages **Clean Architecture** principles and **MVVM** pattern to ensure scalability and testability. Built entirely with **Kotlin** and **Jetpack Compose**, it provides a seamless user experience for browsing, searching, and managing favorite characters from the Rick and Morty universe.

The application features a fully immersive **Dark Mode** UI with advanced animations (Glassmorphism, Sticky Headers, Shared Elements) and robust offline capabilities using local caching.


## Table of Contents

- [Introduction](#introduction)

- [Features](#features)

- [Libraries](#libraries)

- [Project Structure](#project-structures)

- [APK Link](#apk-link)


## <a name="features"></a> Features :

- **Character List**: Infinite scrolling (pagination) grid view with a hero header.

- **Character Detail**: Rich UI with sticky navigation bar with fade animation and horizontal scroll for episodes/recommendations.

- **Advanced Search**:

    - Real-time search with **Debouncing**.

    - Search History (Saved locally).

    - Advanced Filters (Status, Gender, Species, Type).

- **Favorites System**: Add/Remove favorites with real-time synchronization across all screens (Home, Search, Detail).

- **Offline First**: Uses Room Database to cache data, allowing the app to work without an internet connection for visited content.

- **Unit Testing**: Comprehensive unit tests covering Domain (UseCases), Data (Mappers), and Presentation (ViewModels) layers (7 Test Cases).

- **Native Splash Screen**: Implemented using Android 12+ Splash Screen API.



## <a name="libraries"></a> Libraries :

- **Kotlin** - First-class language for Android.

- **Jetpack Compose** (Material3) - Modern toolkit for building native UI.

- **Hilt** - Dependency Injection for managing object lifecycles.

- **Retrofit & OkHttp** - Type-safe HTTP client for API calls.

- **Room Database** - SQLite abstraction layer for local caching and offline support.

- **Coroutines & Flow** - Asynchronous programming and reactive data streams.

- **Coil** - Image loading library backed by Kotlin Coroutines.

- **MockK & JUnit** - Mocking library and testing framework for Unit Tests.

- **Navigation Compose** - Handling navigation between screens.

- **Core Splashscreen** - For backward-compatible splash screen support.



## <a name="project-structures"></a> Project Structure :

The project follows **Clean Architecture** to separate concerns:

* `data`

    * `local` (Room DB, DAO, Entities)

    * `remote` (API Interface, DTOs)

    * `repository` (Repository Implementation)

* `domain`

    * `model` (Data classes used in UI)

    * `repository` (Interfaces)

    * `usecase` (Business Logic)

    * `state` (Resource States: Loading, Success, Error)

* `presentation`

    * `component` (Reusable UI CharacterCard)

    * `screen` (Home, Search, Detail, Favorite Screens)

    * `theme` (Color, Type, Theme definitions)

    * `navigation` (NavGraph and Screen Routes)

* `di` (Dependency Injection Modules)



## <a name="apk-link"></a> APK Link :

You can download the compiled APK file from the link below:

https://drive.google.com/drive/folders/13LhNvNC5UeG5Evoghl0cys3yQ0g1CWgS?usp=sharing
